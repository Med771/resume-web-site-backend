package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;


import java.util.List;

public record GetAboutEducationRes(
        long educationId,

        @NotNull
        List<GetEducationInstitutionRes> studentsInstitution) {
}
