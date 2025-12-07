package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetEducationInstitutionRes(
        @NotNull
        UUID studentId,

        @NotNull
        InstitutionRes studentInstitution) {
}
