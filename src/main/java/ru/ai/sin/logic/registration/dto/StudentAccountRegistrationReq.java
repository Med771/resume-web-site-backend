package ru.ai.sin.logic.registration.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@Schema(name = "StudentAccountRegistrationReq", description = "Регистрация аккаунта студента после подтверждения телефона в Telegram")
public record StudentAccountRegistrationReq(
        @Schema(description = "Логин")
        @NotBlank
        @Size(min = 3, max = 64)
        @Pattern(regexp = "^[a-zA-Z0-9_]{3,64}$", message = "Username must be 3-64 characters, letters, digits or _")
        String username,

        @Schema(description = "Пароль")
        @NotBlank
        String password,

        @Schema(description = "Подтверждение пароля")
        @NotBlank
        String passwordConfirm,

        @Schema(description = "Отображаемое имя (необязательно)")
        @Size(max = 255)
        String name,

        @Schema(description = "Номер телефона (должен совпадать с подтверждённым в Telegram)")
        @NotBlank
        @Pattern(regexp = "\\+?\\d{7,15}", message = "Phone number must contain 7-15 digits and optional + at start")
        String phoneNumber,

        @Schema(description = "ID сессии верификации из POST /verification/phone/start")
        @NotNull
        UUID phoneVerificationId
) {
}
