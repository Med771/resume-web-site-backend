package ru.ai.sin.logic.recruiter.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.config.property.RegistrationProperties;
import ru.ai.sin.config.property.UserProperties;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.logic.recruiter.RecruiterRepo;
import ru.ai.sin.logic.recruiter.registration.dto.RecruiterSelfRegistrationReq;
import ru.ai.sin.logic.registration.ClientIpResolver;
import ru.ai.sin.logic.registration.RegistrationIpRateLimiter;
import ru.ai.sin.logic.registration.RegistrationPasswordPolicy;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.logic.verification.PhoneVerificationService;
import ru.ai.sin.models.enums.RecruiterRegistrationStatus;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterSelfRegistrationServiceImpl implements RecruiterSelfRegistrationService {

    private final RegistrationIpRateLimiter registrationIpRateLimiter;
    private final RegistrationPasswordPolicy passwordPolicy;
    private final RegistrationProperties registrationProperties;
    private final UserProperties userProperties;
    private final PhoneVerificationService phoneVerificationService;

    private final UserRepo userRepo;
    private final RecruiterRepo recruiterRepo;
    private final RecruiterRegistrationRequestRepo registrationRequestRepo;

    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void submit(RecruiterSelfRegistrationReq req, HttpServletRequest httpRequest) {
        registrationIpRateLimiter.check(
                RegistrationIpRateLimiter.RegistrationRateBucket.RECRUITER,
                ClientIpResolver.resolve(httpRequest));

        if (!req.password().equals(req.passwordConfirm())) {
            throw new BadRequestException("Пароли не совпадают");
        }
        passwordPolicy.validate(req.password());

        String username = req.username().trim();
        String email = req.email().trim();
        String phone = trimToNull(req.phoneNumber());

        if (phone != null) {
            phoneVerificationService.requireConfirmed(req.phoneVerificationId(), phone);
        } else {
            throw new BadRequestException("Укажите и подтвердите номер телефона в Telegram");
        }

        if (userRepo.existsByUsername(username)) {
            log.warn("Recruiter registration: username already taken");
            throw conflict();
        }
        if (registrationRequestRepo.existsByStatusAndUsernameIgnoreCase(RecruiterRegistrationStatus.PENDING, username)) {
            log.warn("Recruiter registration: pending username exists");
            throw conflict();
        }
        if (registrationProperties.isReservedUsername(username)) {
            throw new BadRequestException("Этот логин зарезервирован");
        }
        if (userProperties.getLogins() != null) {
            for (UserProperties.Login login : userProperties.getLogins()) {
                if (login.getUsername() != null && login.getUsername().equalsIgnoreCase(username)) {
                    throw new BadRequestException("Этот логин зарезервирован");
                }
            }
        }

        if (recruiterRepo.existsByNormalizedEmail(email)) {
            log.warn("Recruiter registration: email already used by recruiter profile");
            throw conflict();
        }
        if (registrationRequestRepo.existsByStatusAndNormalizedEmail(RecruiterRegistrationStatus.PENDING, email)) {
            log.warn("Recruiter registration: pending email exists");
            throw conflict();
        }

        RecruiterRegistrationRequestEnt ent = new RecruiterRegistrationRequestEnt();
        ent.setUsername(username);
        ent.setPasswordHash(passwordEncoder.encode(req.password()));
        ent.setName(buildDisplayName(req));
        ent.setCompanyName(req.companyName().trim());
        ent.setCity(trimToNull(req.city()));
        ent.setFirstName(trimToNull(req.firstName()));
        ent.setLastName(trimToNull(req.lastName()));
        ent.setMiddleName(trimToNull(req.middleName()));
        ent.setEmail(email);
        ent.setPhoneNumber(phone);
        ent.setTelegramUsername(trimToNull(req.telegramUsername()));
        ent.setPhoneVerificationId(req.phoneVerificationId());
        ent.setMarketingConsent(Boolean.TRUE.equals(req.marketingConsent()));
        ent.setStatus(RecruiterRegistrationStatus.PENDING);

        try {
            registrationRequestRepo.save(ent);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Recruiter registration conflict: {}", ex.getMessage());
            throw conflict();
        }
        log.info("Recruiter registration submitted: id={} username={}", ent.getId(), username);
    }

    private static String buildDisplayName(RecruiterSelfRegistrationReq req) {
        if (req.name() != null && !req.name().isBlank()) {
            return req.name().trim();
        }
        StringBuilder sb = new StringBuilder();
        if (req.lastName() != null && !req.lastName().isBlank()) {
            sb.append(req.lastName().trim());
        }
        if (req.firstName() != null && !req.firstName().isBlank()) {
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(req.firstName().trim());
        }
        if (req.middleName() != null && !req.middleName().isBlank()) {
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(req.middleName().trim());
        }
        return sb.isEmpty() ? null : sb.toString();
    }

    private static BadRequestException conflict() {
        return new BadRequestException(
                "Не удалось отправить заявку. Проверьте данные или дождитесь рассмотрения предыдущей заявки.");
    }

    private static String trimToNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s.trim();
    }
}
