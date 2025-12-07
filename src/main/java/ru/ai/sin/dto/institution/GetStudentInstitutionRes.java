package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.dto.education.EducationRes;

public record GetStudentInstitutionRes(
        EducationRes education,

        @NotNull
        InstitutionRes educationInstitution) {
}
