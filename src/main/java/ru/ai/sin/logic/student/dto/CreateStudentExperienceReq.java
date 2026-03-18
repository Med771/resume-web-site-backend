package ru.ai.sin.logic.student.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record CreateStudentExperienceReq(
        @Positive
        Long companyId,

        @Size(min = 1, max = 255, message = "Company name must be less than 255 characters")
        String companyName,

        @NotBlank
        @Size(min = 1, max = 255, message = "Position must be less than 255 characters")
        String position,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        LocalDate startDate,
        LocalDate endDate
) {
}
