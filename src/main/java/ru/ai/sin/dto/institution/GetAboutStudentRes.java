package ru.ai.sin.dto.institution;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.dto.education.EducationRes;

import java.util.List;
import java.util.UUID;

public record GetAboutStudentRes(
        @NotNull
        UUID studentId,

        @NotNull
        List<GetStudentInstitutionRes> educationsInstitution) {

        public record GetStudentInstitutionRes(
                EducationRes education,

                @NotNull
                InstitutionRes educationInstitution) {
        }
}
