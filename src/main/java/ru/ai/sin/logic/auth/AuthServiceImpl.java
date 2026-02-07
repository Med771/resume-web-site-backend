package ru.ai.sin.logic.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Service;

import ru.ai.sin.logic.auth.dto.*;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.property.JwtProperties;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtHelper jwtHelper;
    private final JwtProperties jwtProperties;

    /**
     * Метод для Login
     */
    public TokenPair login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password()));

        UserDetails userDetails = (UserDetails) auth.getPrincipal();

        if (userDetails == null) {
            throw new NotFoundException("User not found");
        }

        String accessToken = jwtHelper.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtHelper.generateRefreshToken(userDetails.getUsername());

        return new TokenPair(accessToken, refreshToken);
    }

    /**
     * Метод для Refresh
     */
    public String refresh(HttpServletRequest request) {
        String refreshToken = extractRefreshTokenFromCookie(request);
        if (refreshToken == null) {
            throw new BadCredentialsException("No refresh token");
        }

        String username = jwtHelper.getUsernameFromRefreshToken(refreshToken);

        return jwtHelper.generateAccessToken(username);
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;

        return Arrays.stream(cookies)
                .filter(c -> jwtProperties.getCookie().getRefreshTokenName().equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}