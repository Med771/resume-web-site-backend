package ru.ai.sin.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.dto.auth.LoginRequest;
import ru.ai.sin.dto.auth.TokenPair;
import ru.ai.sin.helper.CookieHelper;
import ru.ai.sin.service.impl.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthCnt {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    /**
     * Логин: принимает username/password, возвращает access + refresh в cookies
     */
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        TokenPair tokens = authService.login(request);

        // Устанавливаем cookies
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createAccessTokenCookie(tokens.accessToken()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createRefreshTokenCookie(tokens.refreshToken()).toString());

        return ResponseEntity.ok().build();
    }

    /**
     * Refresh: принимает refresh-токен из cookie, выдаёт новый access-токен
     */
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(HttpServletRequest request, HttpServletResponse response) {
        String newAccessToken = authService.refresh(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createAccessTokenCookie(newAccessToken).toString());

        return ResponseEntity.ok().build();
    }

    /**
     * Logout: очищает cookies и (опционально) отзывает refresh-токен
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.clearAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.clearRefreshTokenCookie().toString());

        return ResponseEntity.ok().build();
    }
}
