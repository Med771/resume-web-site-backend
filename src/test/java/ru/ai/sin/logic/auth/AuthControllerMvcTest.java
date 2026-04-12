package ru.ai.sin.logic.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.web.servlet.MockMvc;
import ru.ai.sin.config.PermitAllWebSecurityTestConfig;
import ru.ai.sin.filter.JwtCookieAuthenticationFilter;
import ru.ai.sin.helper.CookieHelper;
import ru.ai.sin.logic.auth.dto.LoginRequest;
import ru.ai.sin.logic.auth.dto.TokenPair;

import jakarta.servlet.http.HttpServletRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PermitAllWebSecurityTestConfig.class)
class AuthControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private CookieHelper cookieHelper;

    @MockBean
    private JwtCookieAuthenticationFilter jwtCookieAuthenticationFilter;

    @Test
    void login_noContentAndSetsCookies() throws Exception {
        when(authService.login(any(LoginRequest.class)))
                .thenReturn(new TokenPair("access-jwt", "refresh-jwt"));
        when(cookieHelper.createAccessTokenCookie("access-jwt"))
                .thenReturn(ResponseCookie.from("ACCESS_TOKEN", "access-jwt").path("/").build());
        when(cookieHelper.createRefreshTokenCookie("refresh-jwt"))
                .thenReturn(ResponseCookie.from("REFRESH_TOKEN", "refresh-jwt").path("/").build());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"u\",\"password\":\"p\"}"))
                .andExpect(status().isNoContent())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE));

        verify(authService).login(any(LoginRequest.class));
    }

    @Test
    void refresh_noContent() throws Exception {
        when(authService.refresh(any(HttpServletRequest.class))).thenReturn("new-access");
        when(cookieHelper.createAccessTokenCookie("new-access"))
                .thenReturn(ResponseCookie.from("ACCESS_TOKEN", "new-access").path("/").build());

        mockMvc.perform(post("/auth/refresh"))
                .andExpect(status().isNoContent());

        verify(authService).refresh(any(HttpServletRequest.class));
    }

    @Test
    void logout_noContent() throws Exception {
        when(cookieHelper.clearAccessTokenCookie())
                .thenReturn(ResponseCookie.from("ACCESS_TOKEN", "").path("/").maxAge(0).build());
        when(cookieHelper.clearRefreshTokenCookie())
                .thenReturn(ResponseCookie.from("REFRESH_TOKEN", "").path("/").maxAge(0).build());

        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isNoContent());
    }
}
