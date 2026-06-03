package ru.ai.sin.logic.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import ru.ai.sin.config.property.JwtProperties;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.logic.auth.dto.LoginRequest;
import ru.ai.sin.logic.auth.dto.TokenPair;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtHelper jwtHelper;

    @Mock
    private SecurityHelper securityHelper;

    private JwtProperties jwtProperties;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        JwtProperties.CookieProperties cookie = new JwtProperties.CookieProperties();
        cookie.setRefreshTokenName("REFRESH_TOKEN");
        jwtProperties.setCookie(cookie);
        authService = new AuthServiceImpl(authenticationManager, jwtHelper, jwtProperties, securityHelper);
    }

    @Test
    void login_returnsTokenPair() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("alice");
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(jwtHelper.generateAccessToken("alice")).thenReturn("access-jwt");
        when(jwtHelper.generateRefreshToken("alice")).thenReturn("refresh-jwt");

        TokenPair pair = authService.login(new LoginRequest("alice", "secret"));

        assertThat(pair.accessToken()).isEqualTo("access-jwt");
        assertThat(pair.refreshToken()).isEqualTo("refresh-jwt");
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void refresh_throwsWhenNoRefreshCookie() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getCookies()).thenReturn(null);

        assertThatThrownBy(() -> authService.refresh(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("refresh");
    }

    @Test
    void refresh_returnsNewAccessToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie c = new Cookie("REFRESH_TOKEN", "rt-value");
        when(request.getCookies()).thenReturn(new Cookie[]{c});
        when(jwtHelper.getUsernameFromRefreshToken("rt-value")).thenReturn("bob");
        when(jwtHelper.generateAccessToken("bob")).thenReturn("new-access");

        String access = authService.refresh(request);

        assertThat(access).isEqualTo("new-access");
        verify(jwtHelper).generateAccessToken("bob");
    }
}
