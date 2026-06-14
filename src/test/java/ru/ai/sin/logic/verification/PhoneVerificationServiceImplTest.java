package ru.ai.sin.logic.verification;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ai.sin.config.property.MailProperties;
import ru.ai.sin.config.property.TelegramProperties;
import ru.ai.sin.logic.verification.dto.PhoneVerificationStartReq;
import ru.ai.sin.models.enums.PhoneVerificationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PhoneVerificationServiceImplTest {

    @Mock
    private PhoneVerificationRepo phoneVerificationRepo;
    @Mock
    private TelegramBotClient telegramBotClient;
    @Mock
    private VerificationOtpMailer verificationOtpMailer;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private TelegramProperties telegramProperties;
    private MailProperties mailProperties;
    private PhoneVerificationServiceImpl service;

    @BeforeEach
    void setUp() {
        telegramProperties = new TelegramProperties();
        telegramProperties.setVerificationTtlMinutes(15);
        mailProperties = new MailProperties();
        service = new PhoneVerificationServiceImpl(
                phoneVerificationRepo,
                telegramProperties,
                mailProperties,
                telegramBotClient,
                verificationOtpMailer,
                passwordEncoder
        );
    }

    @Test
    void normalizePhone_russianLeading8() {
        assertThat(PhoneVerificationServiceImpl.normalizePhone("+7 (999) 000-11-22")).isEqualTo("79990001122");
        assertThat(PhoneVerificationServiceImpl.normalizePhone("89990001122")).isEqualTo("79990001122");
    }

    @Test
    void confirmWithDevCode_acceptsConfiguredCode() {
        telegramProperties.setAllowDevConfirm(true);
        telegramProperties.setDevConfirmCode("7890");

        PhoneVerificationEnt ent = pendingEntity();
        when(phoneVerificationRepo.findById(ent.getId())).thenReturn(java.util.Optional.of(ent));
        when(phoneVerificationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        var res = service.confirmWithDevCode(ent.getId(), "7890");
        assertThat(res.status()).isEqualTo(PhoneVerificationStatus.CONFIRMED);
    }

    @Test
    void startVerification_sendsOtpWhenEmailAndMailEnabled() {
        telegramProperties.setEnabled(true);
        telegramProperties.setBotUsername("test_bot");
        mailProperties.setEnabled(true);
        mailProperties.setFrom("sender@example.com");

        when(phoneVerificationRepo.save(any())).thenAnswer(invocation -> {
            PhoneVerificationEnt ent = invocation.getArgument(0);
            if (ent.getId() == null) {
                ent.setId(UUID.randomUUID());
            }
            return ent;
        });

        var res = service.startVerification(new PhoneVerificationStartReq("+79991234567", "user@example.com"));

        assertThat(res.verificationId()).isNotNull();
        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> codeCaptor = ArgumentCaptor.forClass(String.class);
        verify(verificationOtpMailer).sendOtp(emailCaptor.capture(), codeCaptor.capture(), eq(15));
        assertThat(emailCaptor.getValue()).isEqualTo("user@example.com");
        assertThat(codeCaptor.getValue()).matches("\\d{4}");
    }

    @Test
    void confirmWithDevCode_acceptsEmailOtpWithoutDevFlag() {
        telegramProperties.setAllowDevConfirm(false);
        String otp = "4321";

        PhoneVerificationEnt ent = pendingEntity();
        ent.setEmail("user@example.com");
        ent.setOtpCodeHash(passwordEncoder.encode(otp));
        when(phoneVerificationRepo.findById(ent.getId())).thenReturn(java.util.Optional.of(ent));
        when(phoneVerificationRepo.save(any())).thenAnswer(i -> i.getArgument(0));

        var res = service.confirmWithDevCode(ent.getId(), otp);
        assertThat(res.status()).isEqualTo(PhoneVerificationStatus.CONFIRMED);
    }

    @Test
    void confirmWithDevCode_rejectsWrongEmailOtp() {
        PhoneVerificationEnt ent = pendingEntity();
        ent.setOtpCodeHash(passwordEncoder.encode("1111"));
        when(phoneVerificationRepo.findById(ent.getId())).thenReturn(java.util.Optional.of(ent));

        assertThatThrownBy(() -> service.confirmWithDevCode(ent.getId(), "9999"))
                .hasMessageContaining("Неверный код");
    }

    @Test
    void startVerification_skipsEmailWhenMailDisabled() {
        telegramProperties.setAllowDevConfirm(true);
        mailProperties.setEnabled(false);

        when(phoneVerificationRepo.save(any())).thenAnswer(invocation -> {
            PhoneVerificationEnt ent = invocation.getArgument(0);
            ent.setId(UUID.randomUUID());
            return ent;
        });

        service.startVerification(new PhoneVerificationStartReq("+79991234567", "user@example.com"));

        verify(verificationOtpMailer, never()).sendOtp(anyString(), anyString(), anyInt());
    }

    @Test
    void resolveBotUsername_stripsAtPrefix() {
        telegramProperties.setEnabled(true);
        telegramProperties.setBotUsername("@my_bot");
        mailProperties.setEnabled(false);

        when(phoneVerificationRepo.save(any())).thenAnswer(invocation -> {
            PhoneVerificationEnt ent = invocation.getArgument(0);
            ent.setId(UUID.randomUUID());
            return ent;
        });

        var res = service.startVerification(new PhoneVerificationStartReq("+79991234567", null));
        assertThat(res.botDeepLink()).contains("https://t.me/my_bot?start=");
    }

    private PhoneVerificationEnt pendingEntity() {
        PhoneVerificationEnt ent = new PhoneVerificationEnt();
        ent.setId(UUID.randomUUID());
        ent.setPhoneNumber("79991234567");
        ent.setStatus(PhoneVerificationStatus.PENDING);
        ent.setCreatedAt(LocalDateTime.now());
        ent.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        return ent;
    }
}
