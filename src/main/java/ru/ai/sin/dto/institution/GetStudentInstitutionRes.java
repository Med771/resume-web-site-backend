package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetStudentInstitutionRes(
        long educationId,

        @NotNull
        List<InstitutionRes> educationInstitution) {
}
