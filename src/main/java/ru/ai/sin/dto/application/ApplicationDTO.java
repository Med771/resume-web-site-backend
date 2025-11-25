package ru.ai.sin.dto.application;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import ru.ai.sin.entity.model.ResultEnum;

import java.util.UUID;

public record ApplicationDTO(
        long id,

        @NotBlank
        @Pattern(regexp = "-?\\d{1,32}", message = "Chat ID must contain 1-32 digits and optional - at start")
        String chatId,

        @NotBlank
        String historyPath,

        @NotNull
        ResultEnum result,

        @NotBlank
        @Size(min = 1, max = 255, message = "Result message must be less than 255 characters")
        String resultMessage,

        @NotNull
        UUID recruiterId,

        @NotNull
        UUID studentId) {
}
