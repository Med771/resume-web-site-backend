package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;

public record GetEducationInstitutionRes(
        @NotNull
        StudentEnt student,

        @NotNull
        List<InstitutionRes> studentInstitution) {
}
