package ru.ai.sin.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import ru.ai.sin.property.JwtProperties;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class CookieHelper {

    private final JwtProperties jwtProperties;

    private ResponseCookie createCookie(String name, String value, Duration duration) {
        JwtProperties.CookieProperties cookieProps = jwtProperties.getCookie();

        return ResponseCookie.from(name, value)
                .httpOnly(cookieProps.isHttpOnly())
                .secure(cookieProps.isSecure())
                .sameSite(cookieProps.getSameSite())
                .path(cookieProps.getPath())
                .maxAge(duration)
                .build();
    }

    /**
     * Создаёт новый access token cookie
     */
    public ResponseCookie createAccessTokenCookie(String token) {
        return createCookie(
                jwtProperties.getCookie().getAccessTokenName(),
                token,
                Duration.ofSeconds(jwtProperties.getAccessTokenTtl()));
    }

    /**
     * Создаёт новый refresh token cookie
     */
    public ResponseCookie createRefreshTokenCookie(String token) {
        return createCookie(
                jwtProperties.getCookie().getRefreshTokenName(),
                token,
                Duration.ofSeconds(jwtProperties.getRefreshTokenTtl()));
    }

    /**
     * Очищает access token cookie
     */
    public ResponseCookie clearAccessTokenCookie() {
        return createCookie(jwtProperties.getCookie().getAccessTokenName(), "", Duration.ZERO);
    }

    /**
     * Очищает refresh token cookie
     */
    public ResponseCookie clearRefreshTokenCookie() {
        return createCookie(jwtProperties.getCookie().getRefreshTokenName(), "", Duration.ZERO);
    }
}
