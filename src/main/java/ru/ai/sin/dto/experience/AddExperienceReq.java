package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AddExperienceReq(
        long companyId,
        long studentId,

        @NotBlank
        @Size(min = 1, max = 255, message = "Name must be less than 255 characters")
        String position,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        @NotNull
        LocalDate startDate,

        @NotNull
        LocalDate endDate) {
}
