package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;

public record GetStudentInstitutionRes(
        long educationId,

        @NotNull
        InstitutionRes educationInstitution) {
}
