package ru.ai.sin.dto.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record StudentRes(
        @NotNull
        UUID id,

        @NotNull
        String speciality,

        @Size(min = 1, max = 255, message = "First name must be less than 255 characters")
        String fullName,

        @NotNull
        String chatId
) {
}
