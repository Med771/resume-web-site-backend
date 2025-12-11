package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.dto.company.CompanyRes;

import java.util.List;
import java.util.UUID;

public record GetAboutStudentRes(
        @NotNull
        UUID studentId,

        @NotNull
        List<GetStudentExperienceRes> companyExperiences) {

        public record GetStudentExperienceRes(
                @NotNull
                CompanyRes company,

                @NotNull
                ExperienceRes experience) {
        }

}
