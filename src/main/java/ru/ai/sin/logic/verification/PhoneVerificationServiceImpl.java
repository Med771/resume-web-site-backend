package ru.ai.sin.logic.verification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.config.property.TelegramProperties;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.verification.dto.PhoneVerificationStartReq;
import ru.ai.sin.logic.verification.dto.PhoneVerificationStartRes;
import ru.ai.sin.logic.verification.dto.PhoneVerificationStatusRes;
import ru.ai.sin.models.enums.PhoneVerificationStatus;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneVerificationServiceImpl implements PhoneVerificationService {

    private final PhoneVerificationRepo phoneVerificationRepo;
    private final TelegramProperties telegramProperties;
    private final TelegramBotClient telegramBotClient;

    @Override
    @Transactional
    public PhoneVerificationStartRes startVerification(PhoneVerificationStartReq req) {
        ensureTelegramEnabledOrDev();

        String normalized = normalizePhone(req.phoneNumber());
        PhoneVerificationEnt ent = new PhoneVerificationEnt();
        ent.setPhoneNumber(normalized);
        ent.setStatus(PhoneVerificationStatus.PENDING);
        ent.setCreatedAt(LocalDateTime.now());
        ent.setExpiresAt(LocalDateTime.now().plusMinutes(telegramProperties.getVerificationTtlMinutes()));
        ent = phoneVerificationRepo.save(ent);

        String botUsername = resolveBotUsername();
        String deepLink = "https://t.me/" + botUsername + "?start=" + ent.getId();

        return new PhoneVerificationStartRes(
                ent.getId(),
                botUsername,
                deepLink,
                telegramProperties.getVerificationTtlMinutes()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PhoneVerificationStatusRes getStatus(UUID verificationId) {
        PhoneVerificationEnt ent = phoneVerificationRepo.findById(verificationId)
                .orElseThrow(() -> new NotFoundException("Сессия верификации не найдена"));
        return new PhoneVerificationStatusRes(ent.getId(), resolveEffectiveStatus(ent));
    }

    @Override
    @Transactional(readOnly = true)
    public void requireConfirmed(UUID verificationId, String phoneNumber) {
        PhoneVerificationEnt ent = phoneVerificationRepo.findById(verificationId)
                .orElseThrow(() -> new BadRequestException("Подтвердите номер телефона в Telegram"));

        PhoneVerificationStatus status = resolveEffectiveStatus(ent);
        if (status != PhoneVerificationStatus.CONFIRMED) {
            throw new BadRequestException("Номер телефона ещё не подтверждён в Telegram");
        }
        if (!normalizePhone(phoneNumber).equals(ent.getPhoneNumber())) {
            throw new BadRequestException("Номер телефона не совпадает с подтверждённым");
        }
    }

    @Override
    @Transactional
    public PhoneVerificationStatusRes confirmWithDevCode(UUID verificationId, String code) {
        if (!telegramProperties.isAllowDevConfirm()) {
            throw new BadRequestException("Подтверждение кодом недоступно");
        }
        String expected = telegramProperties.getDevConfirmCode();
        if (expected == null || expected.isBlank()) {
            throw new BadRequestException("Тестовый код не настроен");
        }
        if (!expected.equals(code != null ? code.trim() : "")) {
            throw new BadRequestException("Неверный код подтверждения");
        }

        PhoneVerificationEnt ent = phoneVerificationRepo.findById(verificationId)
                .orElseThrow(() -> new NotFoundException("Сессия верификации не найдена"));

        PhoneVerificationStatus status = resolveEffectiveStatus(ent);
        if (status == PhoneVerificationStatus.EXPIRED) {
            throw new BadRequestException("Время подтверждения истекло");
        }
        if (status == PhoneVerificationStatus.CONFIRMED) {
            return new PhoneVerificationStatusRes(ent.getId(), PhoneVerificationStatus.CONFIRMED);
        }

        ent.setStatus(PhoneVerificationStatus.CONFIRMED);
        ent.setConfirmedAt(LocalDateTime.now());
        phoneVerificationRepo.save(ent);
        log.warn("Phone verified via dev code: verificationId={} phone={}", ent.getId(), ent.getPhoneNumber());
        return new PhoneVerificationStatusRes(ent.getId(), PhoneVerificationStatus.CONFIRMED);
    }

    @Override
    @Transactional
    @SuppressWarnings("unchecked")
    public void handleWebhookUpdate(Map<String, Object> update) {
        if (!telegramProperties.isEnabled()) {
            return;
        }
        Object messageObj = update.get("message");
        if (!(messageObj instanceof Map<?, ?> message)) {
            return;
        }

        Object chatObj = message.get("chat");
        if (!(chatObj instanceof Map<?, ?> chat)) {
            return;
        }
        Object chatIdObj = chat.get("id");
        if (chatIdObj == null) {
            return;
        }
        long chatId = Long.parseLong(chatIdObj.toString());

        Object contactObj = message.get("contact");
        if (contactObj instanceof Map<?, ?> contact) {
            handleContact(chatId, contact);
            return;
        }

        Object textObj = message.get("text");
        if (textObj instanceof String text && text.startsWith("/start")) {
            handleStart(chatId, text);
        }
    }

    private void handleStart(long chatId, String text) {
        String[] parts = text.trim().split("\\s+", 2);
        if (parts.length < 2) {
            telegramBotClient.sendMessage(chatId,
                    "Для подтверждения номера откройте ссылку с сайта регистрации.");
            return;
        }
        UUID verificationId;
        try {
            verificationId = UUID.fromString(parts[1].trim());
        } catch (IllegalArgumentException ex) {
            telegramBotClient.sendMessage(chatId, "Неверная ссылка верификации.");
            return;
        }

        PhoneVerificationEnt ent = phoneVerificationRepo.findById(verificationId).orElse(null);
        if (ent == null || resolveEffectiveStatus(ent) != PhoneVerificationStatus.PENDING) {
            telegramBotClient.sendMessage(chatId, "Сессия верификации не найдена или уже завершена.");
            return;
        }

        ent.setTelegramChatId(chatId);
        phoneVerificationRepo.save(ent);

        telegramBotClient.sendContactRequest(chatId,
                "Нажмите кнопку ниже и поделитесь номером телефона для подтверждения.");
    }

    private void handleContact(long chatId, Map<?, ?> contact) {
        Object phoneObj = contact.get("phone_number");
        Object userIdObj = contact.get("user_id");
        if (phoneObj == null) {
            return;
        }
        String phone = normalizePhone(phoneObj.toString());
        String telegramUserId = userIdObj != null ? userIdObj.toString() : null;

        PhoneVerificationEnt pending = phoneVerificationRepo
                .findByTelegramChatIdAndStatus(chatId, PhoneVerificationStatus.PENDING)
                .filter(v -> resolveEffectiveStatus(v) == PhoneVerificationStatus.PENDING)
                .orElse(null);

        if (pending != null && !phone.equals(pending.getPhoneNumber())) {
            telegramBotClient.sendMessage(chatId,
                    "Номер телефона не совпадает с указанным на сайте. Начните верификацию заново.");
            return;
        }

        if (pending == null) {
            telegramBotClient.sendMessage(chatId,
                    "Не найдена активная сессия верификации для этого номера. Начните с сайта.");
            return;
        }

        pending.setStatus(PhoneVerificationStatus.CONFIRMED);
        pending.setConfirmedAt(LocalDateTime.now());
        pending.setTelegramUserId(telegramUserId);
        phoneVerificationRepo.save(pending);

        telegramBotClient.removeKeyboard(chatId, "Номер телефона подтверждён. Вернитесь на сайт и завершите регистрацию.");
        log.info("Phone verified: verificationId={} phone={}", pending.getId(), phone);
    }

    private PhoneVerificationStatus resolveEffectiveStatus(PhoneVerificationEnt ent) {
        if (ent.getStatus() == PhoneVerificationStatus.PENDING && ent.getExpiresAt().isBefore(LocalDateTime.now())) {
            return PhoneVerificationStatus.EXPIRED;
        }
        return ent.getStatus();
    }

    private void ensureTelegramEnabled() {
        if (!telegramProperties.isEnabled()) {
            throw new BadRequestException("Верификация через Telegram временно недоступна");
        }
        if (telegramProperties.getBotUsername() == null || telegramProperties.getBotUsername().isBlank()) {
            throw new BadRequestException("Telegram-бот не настроен");
        }
        if (telegramProperties.getBotToken() == null || telegramProperties.getBotToken().isBlank()) {
            throw new BadRequestException("Telegram-бот не настроен");
        }
    }

    private void ensureTelegramEnabledOrDev() {
        if (telegramProperties.isAllowDevConfirm()) {
            return;
        }
        ensureTelegramEnabled();
    }

    private String resolveBotUsername() {
        if (telegramProperties.getBotUsername() != null && !telegramProperties.getBotUsername().isBlank()) {
            return telegramProperties.getBotUsername();
        }
        if (telegramProperties.isAllowDevConfirm()) {
            return "dev_bot";
        }
        throw new BadRequestException("Telegram-бот не настроен");
    }

    static String normalizePhone(String phone) {
        if (phone == null) {
            return "";
        }
        String digits = phone.replaceAll("\\D", "");
        if (digits.startsWith("8") && digits.length() == 11) {
            return "7" + digits.substring(1);
        }
        return digits;
    }
}
