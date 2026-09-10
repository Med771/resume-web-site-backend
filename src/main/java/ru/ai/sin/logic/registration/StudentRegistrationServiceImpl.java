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
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.logic.verification.PhoneVerificationService;
import ru.ai.sin.models.embeddables.ContactInformation;
import ru.ai.sin.models.embeddables.UserInformation;
import ru.ai.sin.models.enums.AccountStatus;
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
    private final StudentRepo studentRepo;
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

        StudentEnt student = studentRepo.save(createDraftStudent(req, phone));

        UserEnt user = new UserEnt(
                RoleEnum.STUDENT,
                buildDisplayName(req),
                username,
                passwordEncoder.encode(req.password())
        );
        user.setPhoneVerified(true);
        user.setAccountStatus(AccountStatus.PENDING_APPROVAL);
        user.setRegistrationPhone(phone);
        user.setRegistrationEmail(emptyToNull(req.email()));
        user.setStudent(student);

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
        log.info("Student account registered: username={} studentId={}", username, student.getId());
        return new TokenPair(access, refresh);
    }

    private static StudentEnt createDraftStudent(StudentAccountRegistrationReq req, String phone) {
        StudentEnt student = new StudentEnt();
        student.setCity(emptyToNull(req.city()));
        student.setBirthDate(req.birthDate());
        student.setCourse(req.course());
        student.setCatalogVisible(false);
        student.setPublicProfileConsent(false);

        UserInformation userInfo = new UserInformation();
        userInfo.setFirstName(emptyToNull(req.firstName()));
        userInfo.setLastName(emptyToNull(req.lastName()));
        userInfo.setEmail(emptyToNull(req.email()));
        student.setUserInformation(userInfo);

        ContactInformation contact = new ContactInformation();
        contact.setPhoneNumber(phone);
        student.setContactInformation(contact);

        return student;
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

    private static String buildDisplayName(StudentAccountRegistrationReq req) {
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
}
