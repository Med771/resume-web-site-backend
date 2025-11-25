package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.EducationEnt;

import java.util.List;

public record GetStudentInstitutionRes(
        @NotNull
        EducationEnt education,

        @NotNull
        List<InstitutionRes> educationInstitution) {
}
