package ru.ai.sin.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    GUEST("GUEST"),
    USER("USER"),
    STUDENT("STUDENT"),
    ADMIN("ADMIN");

    private final String role;

    public static RoleEnum fromRole(String role) {
        for (RoleEnum status: values()) {
            if (status.getRole().equals(role)) return status;
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
