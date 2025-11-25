package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.EducationEnt;
import ru.ai.sin.entity.StudentEnt;

public record GetInstitutionRes(
        @NotNull
        EducationEnt education,

        @NotNull
        StudentEnt student,

        @NotNull
        InstitutionRes institution) {
}
