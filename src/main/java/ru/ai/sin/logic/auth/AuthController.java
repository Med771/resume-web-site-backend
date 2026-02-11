package ru.ai.sin.logic.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.logic.auth.dto.*;

import ru.ai.sin.helper.CookieHelper;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    /**
     * Логин: принимает username/password, возвращает access + refresh в cookies
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        TokenPair tokens = authService.login(request);

        // Устанавливаем cookies
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createAccessTokenCookie(tokens.accessToken()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createRefreshTokenCookie(tokens.refreshToken()).toString());
    }

    /**
     * Refresh: принимает refresh-токен из cookie, выдаёт новый access-токен
     */
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String newAccessToken = authService.refresh(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createAccessTokenCookie(newAccessToken).toString());
    }

    /**
     * Logout: очищает cookies и (опционально) отзывает refresh-токен
     */
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.clearAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.clearRefreshTokenCookie().toString());
    }
}
