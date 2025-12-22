package ru.ai.sin.entity.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    GUEST("guest"),
    USER("user"),
    ADMIN("admin");

    private final String role;

    public static RoleEnum fromRole(String role) {
        for (RoleEnum status: values()) {
            if (status.getRole().equals(role)) return status;
        }
        throw new IllegalArgumentException("Unknown role: " + role);
    }
}
