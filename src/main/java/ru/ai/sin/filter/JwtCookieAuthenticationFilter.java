package ru.ai.sin.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.ai.sin.helper.CookieHelper;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.property.JwtProperties;

import java.util.Arrays;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtCookieAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final CookieHelper cookieHelper;

    private final JwtProperties jwtProperties;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) {

        try {
            String accessToken = extractCookie(request, jwtProperties.getCookie().getAccessTokenName());
            String refreshToken = extractCookie(request, jwtProperties.getCookie().getRefreshTokenName());

            if (accessToken != null && !accessToken.isBlank()) {
                String username = jwtHelper.getUsernameFromAccessToken(accessToken);
                setAuthentication(username, request);
                filterChain.doFilter(request, response);
                return;
            }

            if (refreshToken != null && !refreshToken.isBlank()) {
                String username = jwtHelper.getUsernameFromRefreshToken(refreshToken);
                if (username != null) {
                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                    String newAccessToken = jwtHelper.generateAccessToken(userDetails.getUsername());

                    ResponseCookie newAccessCookie = cookieHelper.createAccessTokenCookie(newAccessToken);
                    response.addHeader(HttpHeaders.SET_COOKIE, newAccessCookie.toString());

                    setAuthentication(username, request);
                }
            }

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.debug("Access token expired: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception e) {
            log.error("Authentication error", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void setAuthentication(String username, HttpServletRequest request) {
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());

            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
    }

    /**
     * Извлекает значение cookie по имени
     */
    private String extractCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null);
    }
}