package ru.ai.sin.logic.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthMeDTO", description = "Текущая сессия (роль и логин).")
public record AuthMeDTO(
        @Schema(description = "Логин пользователя")
        String username,
        @Schema(description = "Роль: STUDENT, RECRUITER, ADMIN")
        String role,
        @Schema(description = "Статус аккаунта: PENDING_APPROVAL, APPROVED, REJECTED")
        String accountStatus,
        @Schema(description = "Подсказки при создании резюме/вакансий отключены")
        boolean hintsDisabled
) {
}
