package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record GetAboutStudentRes(
        @NotNull
        UUID studentId,

        @NotNull
        List<GetStudentInstitutionRes> educationsInstitution) {
}
