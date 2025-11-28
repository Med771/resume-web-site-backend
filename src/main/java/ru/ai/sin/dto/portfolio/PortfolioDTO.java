package ru.ai.sin.dto.portfolio;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

public record PortfolioDTO(
        long id,

        @NotBlank
        @Size(min = 1, max = 255, message = "Name must be less than 255 characters")
        String name,

        @Size(min = 1, max = 255, message = "Name must be less than 255 characters")
        String link,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        UUID studentId) {
}
