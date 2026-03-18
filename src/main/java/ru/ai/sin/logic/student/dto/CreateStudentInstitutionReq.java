package ru.ai.sin.logic.student.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateStudentInstitutionReq(
        @Positive
        Long educationId,

        @Size(min = 1, max = 255, message = "Institution must be less than 255 characters")
        String institution,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        @Size(min = 1, max = 255, message = "Web Url must be less than 255 characters")
        String webUrl,

        @Min(1900)
        @Max(2100)
        int startYear,

        @Min(1900)
        @Max(2100)
        int endYear
) {
}
