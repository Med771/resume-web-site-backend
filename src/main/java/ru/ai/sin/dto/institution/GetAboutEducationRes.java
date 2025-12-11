package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;


import java.util.List;
import java.util.UUID;

public record GetAboutEducationRes(
        long educationId,

        @NotNull
        List<GetEducationInstitutionRes> studentsInstitution) {

        public record GetEducationInstitutionRes(
                @NotNull
                UUID studentId,

                @NotNull
                InstitutionRes studentInstitution) {
        }
}
