package ru.ai.sin.helper;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Component;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.models.enums.RoleEnum;

@Component
public class SecurityHelper {
    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        throw new UsernameNotFoundException("User not found");
    }

    public void checkAdminRoleForFilter() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            throw new AccessDeniedException("Authentication required");
        }

        UserEnt userEnt = (UserEnt) auth.getPrincipal();

        if (userEnt.getRole() != RoleEnum.ADMIN) {
            throw new AccessDeniedException("Only admins can access experiences");
        }
    }
}
