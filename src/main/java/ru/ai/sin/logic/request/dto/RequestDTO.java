package ru.ai.sin.logic.request.dto;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.models.enums.ResultEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestDTO(
        long id,

        @NotNull
        UUID appChatId,

        @NotNull
        ResultEnum result,

        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        String studentResponseText,

        @NotNull
        UUID recruiterId,

        @NotNull
        UUID studentId
) {
}
