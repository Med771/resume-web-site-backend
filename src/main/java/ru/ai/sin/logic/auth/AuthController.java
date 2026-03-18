package ru.ai.sin.logic.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Auth", description = "Операции аутентификации и управления сессией")
public class AuthController {

    private final AuthService authService;
    private final CookieHelper cookieHelper;

    @Operation(summary = "Вход в систему", description = "Проверяет логин/пароль и устанавливает access и refresh токены в cookie")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void login(@RequestBody LoginRequest request, HttpServletResponse response) {
        TokenPair tokens = authService.login(request);

        // Устанавливаем cookies
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createAccessTokenCookie(tokens.accessToken()).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createRefreshTokenCookie(tokens.refreshToken()).toString());
    }

    @Operation(summary = "Обновить access токен", description = "Использует refresh токен из cookie и выдает новый access токен")
    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void refresh(HttpServletRequest request, HttpServletResponse response) {
        String newAccessToken = authService.refresh(request);

        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.createAccessTokenCookie(newAccessToken).toString());
    }

    @Operation(summary = "Выход из системы", description = "Очищает access и refresh cookie")
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletResponse response) {
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.clearAccessTokenCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieHelper.clearRefreshTokenCookie().toString());
    }
}
