package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record AddInstitutionReq(
        long educationId,
        long studentId,

        @Min(1900)
        @Max(2100)
        int startYear,

        @Min(1900)
        @Max(2100)
        int endYear) {
}
