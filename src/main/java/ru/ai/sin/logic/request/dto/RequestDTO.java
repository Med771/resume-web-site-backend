package ru.ai.sin.logic.request.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.ai.sin.entity.model.ResultEnum;

import java.util.UUID;

public record RequestDTO(
        long id,

        @Size(min = 1, max = 16, message = "Chat id must be less than 16 characters")
        String chatId,

        @NotNull
        ResultEnum result,

        @Size(min = 1, max = 255, message = "Chat url must be less than 255 characters")
        String chatUrl,

        @NotNull
        UUID recruiterId,

        @Size(min = 1, max = 16, message = "Chat id must be less than 16 characters")
        String recruiterTelegramUserId,

        @NotNull
        UUID studentId,

        @Size(min = 1, max = 16, message = "Chat id must be less than 16 characters")
        String studentTelegramUserId,

        String studentResponseText,

        boolean hasRecruiterMessage,

        boolean hasStudentMessage
) {
}
