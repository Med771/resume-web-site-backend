package ru.ai.sin.helper;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;
import ru.ai.sin.models.enums.RoleEnum;

import java.util.Optional;

@Component
public class SecurityHelper {
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        throw new UsernameNotFoundException("User not found");
    }

    /** Логин из JWT / сессии, если пользователь аутентифицирован */
    public Optional<String> getCurrentUsernameOptional() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }
        if (auth.getPrincipal() instanceof UserDetails userDetails) {
            return Optional.of(userDetails.getUsername());
        }
        return Optional.empty();
    }

    /**
     * Проверка роли администратора по authorities (principal — {@link UserDetails}, не сущность БД).
     */
    public void checkAdminRoleForFilter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        String adminRole = "ROLE_" + RoleEnum.ADMIN.getRole();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> adminRole.equals(a.getAuthority()));

        if (!isAdmin) {
            throw new AccessDeniedException("Only admins can access filter");
        }
    }
}
