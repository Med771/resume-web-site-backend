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
import ru.ai.sin.logic.registration.dto.StudentAccountRegistrationReq;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.logic.verification.PhoneVerificationService;

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
    private PhoneVerificationService phoneVerificationService;
    @Mock
    private UserRepo userRepo;
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
        lenient().when(registrationProperties.isReservedUsername(anyString())).thenReturn(false);
        lenient().when(userProperties.getLogins()).thenReturn(List.of());
        lenient().when(httpRequest.getHeader("X-Forwarded-For")).thenReturn(null);
        lenient().when(httpRequest.getRemoteAddr()).thenReturn("127.0.0.1");

        service = new StudentRegistrationServiceImpl(
                registrationIpRateLimiter,
                passwordPolicy,
                registrationProperties,
                userProperties,
                phoneVerificationService,
                userRepo,
                passwordEncoder,
                authenticationManager,
                jwtHelper
        );
    }

    @Test
    void register_passwordMismatch_throwsBadRequest() {
        StudentAccountRegistrationReq req = new StudentAccountRegistrationReq(
                "user1", "SecurePass123", "other", null, null, null, null, "+79990001122", UUID.randomUUID());

        assertThatThrownBy(() -> service.registerAndIssueTokens(req, httpRequest))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Пароли");
    }

    @Test
    void register_reservedUsername_throwsBadRequest() {
        when(registrationProperties.isReservedUsername("root")).thenReturn(true);
        StudentAccountRegistrationReq req = new StudentAccountRegistrationReq(
                "root", "SecurePass123", "SecurePass123", null, null, null, null, "+79990001122", UUID.randomUUID());

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

    private static StudentAccountRegistrationReq baseReq() {
        return new StudentAccountRegistrationReq(
                "newuser_x",
                "SecurePass123",
                "SecurePass123",
                null,
                "Иван",
                "Иванов",
                null,
                "+79990001122",
                UUID.randomUUID()
        );
    }
}
