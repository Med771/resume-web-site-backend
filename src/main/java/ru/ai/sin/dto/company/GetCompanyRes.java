package ru.ai.sin.dto.company;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record GetCompanyRes(
        long id,

        @NotBlank
        @Size(min = 1, max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotNull
        LocalDateTime createdAt,
        @NotNull
        LocalDateTime updatedAt) {
}
