package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record InstitutionRes(
        long id,

        @Min(1900)
        @Max(2100)
        int startYear,

        @Min(1900)
        @Max(2100)
        int endYear,

        @NotNull
        LocalDateTime createdAt,
        @NotNull
        LocalDateTime updatedAt) {
}
