package ru.ai.sin.logic.auth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ru.ai.sin.logic.auth.dto.*;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.registration.RegistrationPasswordPolicy;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.config.property.JwtProperties;
import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.enums.AccountStatus;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtHelper jwtHelper;
    private final JwtProperties jwtProperties;
    private final SecurityHelper securityHelper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RegistrationPasswordPolicy passwordPolicy;

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

    @Override
    public AuthMeDTO getCurrentSession() {
        String username = securityHelper.getCurrentUsernameOptional()
                .orElseThrow(() -> new BadCredentialsException("Not authenticated"));
        String role = securityHelper.getCurrentRoleOptional()
                .orElseThrow(() -> new BadCredentialsException("Not authenticated"));
        UserEnt user = userRepo.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Not authenticated"));
        AccountStatus status = user.getAccountStatus() != null ? user.getAccountStatus() : AccountStatus.APPROVED;
        return new AuthMeDTO(username, role, status.getCode(), user.isHintsDisabled());
    }

    @Override
    public void changePassword(ChangePasswordReq req) {
        String username = securityHelper.getCurrentUsername();
        UserEnt user = userRepo.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (user.getPasswordHash() == null
                || !passwordEncoder.matches(req.currentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("Неверный текущий пароль");
        }
        passwordPolicy.validate(req.newPassword());
        user.setPasswordHash(passwordEncoder.encode(req.newPassword()));
        userRepo.save(user);
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