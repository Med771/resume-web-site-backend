package ru.ai.sin.logic.registration;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import ru.ai.sin.config.property.RegistrationProperties;
import ru.ai.sin.config.property.UserProperties;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.TooManyRequestsException;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.logic.registration.dto.StudentSelfRegistrationReq;
import ru.ai.sin.logic.skill.SkillRepo;
import ru.ai.sin.logic.student.StudentCvAttachmentService;
import ru.ai.sin.logic.student.StudentMapper;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.tools.SpecialityTools;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentRegistrationServiceImplTest {

    @Mock
    private RegistrationIpRateLimiter registrationIpRateLimiter;
    @Mock
    private RegistrationPasswordPolicy passwordPolicy;
    @Mock
    private RegistrationProperties registrationProperties;
    @Mock
    private UserProperties userProperties;
    @Mock
    private UserRepo userRepo;
    @Mock
    private StudentRepo studentRepo;
    @Mock
    private SkillRepo skillRepo;
    @Mock
    private StudentMapper studentMapper;
    @Mock
    private SpecialityTools specialityTools;
    @Mock
    private StudentCvAttachmentService studentCvAttachmentService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtHelper jwtHelper;
    @Mock
    private HttpServletRequest httpRequest;

    private StudentRegistrationServiceImpl service;

    @BeforeEach
    void setUp() {
        lenient().when(registrationProperties.getMaxSkillsPerProfile()).thenReturn(30);
        lenient().when(registrationProperties.getMaxExperiencesInRegistration()).thenReturn(12);
        lenient().when(registrationProperties.getMaxInstitutionsInRegistration()).thenReturn(8);
        lenient().when(registrationProperties.isReservedUsername(anyString())).thenReturn(false);
        lenient().when(userProperties.getLogins()).thenReturn(List.of());
        lenient().when(httpRequest.getHeader("X-Forwarded-For")).thenReturn(null);
        lenient().when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        service = new StudentRegistrationServiceImpl(
                registrationIpRateLimiter,
                passwordPolicy,
                registrationProperties,
                userProperties,
                userRepo,
                studentRepo,
                skillRepo,
                studentMapper,
                specialityTools,
                studentCvAttachmentService,
                passwordEncoder,
                authenticationManager,
                jwtHelper
        );
    }

    @Test
    void register_passwordMismatch_throwsBadRequest() {
        StudentSelfRegistrationReq b = baseReq();
        StudentSelfRegistrationReq req = new StudentSelfRegistrationReq(
                b.username(),
                b.password(),
                "other",
                b.name(),
                b.city(),
                b.hhLink(),
                b.birthDate(),
                b.bio(),
                b.busyness(),
                b.firstName(),
                b.lastName(),
                b.email(),
                b.phoneNumber(),
                b.telegramUsername(),
                b.specialityId(),
                b.skillsIds(),
                b.experiences(),
                b.institutions()
        );

        assertThatThrownBy(() -> service.registerAndIssueTokens(req, httpRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Пароли");
    }

    @Test
    void register_reservedUsername_throwsBadRequest() {
        when(registrationProperties.isReservedUsername("root")).thenReturn(true);
        StudentSelfRegistrationReq b = baseReq();
        StudentSelfRegistrationReq req = new StudentSelfRegistrationReq(
                "root",
                "SecurePass123",
                "SecurePass123",
                b.name(),
                b.city(),
                b.hhLink(),
                b.birthDate(),
                b.bio(),
                b.busyness(),
                b.firstName(),
                b.lastName(),
                b.email(),
                b.phoneNumber(),
                b.telegramUsername(),
                b.specialityId(),
                b.skillsIds(),
                b.experiences(),
                b.institutions()
        );

        assertThatThrownBy(() -> service.registerAndIssueTokens(req, httpRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("зарезервирован");
    }

    @Test
    void register_rateLimited_throws429() {
        org.mockito.Mockito.doThrow(new TooManyRequestsException("stop"))
                .when(registrationIpRateLimiter).check(any(RegistrationIpRateLimiter.RegistrationRateBucket.class), anyString());

        assertThatThrownBy(() -> service.registerAndIssueTokens(baseReq(), httpRequest))
                .isInstanceOf(TooManyRequestsException.class);
    }

    private static StudentSelfRegistrationReq baseReq() {
        return new StudentSelfRegistrationReq(
                "newuser_x",
                "SecurePass123",
                "SecurePass123",
                "Имя",
                "Moscow",
                "https://hh.ru/x",
                LocalDate.of(2001, 1, 15),
                "bio",
                BusynessEnum.FREE,
                "Иван",
                "Иванов",
                "ivan+" + UUID.randomUUID() + "@test.local",
                "+79990001122",
                "ivan_tg",
                1L,
                List.of(),
                null,
                null
        );
    }
}
