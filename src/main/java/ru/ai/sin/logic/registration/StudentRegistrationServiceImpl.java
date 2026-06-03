package ru.ai.sin.logic.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.config.property.RegistrationProperties;
import ru.ai.sin.config.property.UserProperties;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.logic.auth.dto.TokenPair;
import ru.ai.sin.logic.registration.dto.StudentAccountRegistrationReq;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.logic.verification.PhoneVerificationService;
import ru.ai.sin.models.enums.RoleEnum;

import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentRegistrationServiceImpl implements StudentRegistrationService {

    private final RegistrationIpRateLimiter registrationIpRateLimiter;
    private final RegistrationPasswordPolicy passwordPolicy;
    private final RegistrationProperties registrationProperties;
    private final UserProperties userProperties;
    private final PhoneVerificationService phoneVerificationService;

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    @Override
    @Transactional
    public TokenPair registerAndIssueTokens(StudentAccountRegistrationReq req, HttpServletRequest httpRequest) {
        registrationIpRateLimiter.check(
                RegistrationIpRateLimiter.RegistrationRateBucket.STUDENT,
                ClientIpResolver.resolve(httpRequest));

        if (!req.password().equals(req.passwordConfirm())) {
            throw new BadRequestException("Пароли не совпадают");
        }
        passwordPolicy.validate(req.password());

        String username = req.username().trim();
        String phone = req.phoneNumber().trim();

        phoneVerificationService.requireConfirmed(req.phoneVerificationId(), phone);

        if (userRepo.existsByUsername(username)) {
            log.warn("Student registration: username already exists");
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

        UserEnt user = new UserEnt(
                RoleEnum.STUDENT,
                emptyToNull(req.name()),
                username,
                passwordEncoder.encode(req.password())
        );
        user.setPhoneVerified(true);

        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.warn("User registration data conflict: {}", ex.getMessage());
            throw conflict();
        }

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, req.password()));
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String access = jwtHelper.generateAccessToken(principal.getUsername());
        String refresh = jwtHelper.generateRefreshToken(principal.getUsername());
        log.info("Student account registered: username={} (resume via onboarding)", username);
        return new TokenPair(access, refresh);
    }

    private static BadRequestException conflict() {
        return new BadRequestException(
                "Не удалось завершить регистрацию. Проверьте данные или войдите, если аккаунт уже есть.");
    }

    private static String emptyToNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s.trim();
    }
}
