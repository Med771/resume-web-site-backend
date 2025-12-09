package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record GetAboutCompanyRes(
        @NotNull
        long companyId,

        @NotNull
        List<GetCompanyExperienceRes> studentsExperiences) {

        public record GetCompanyExperienceRes(
                @NotNull
                UUID studentId,

                @NotNull
                ExperienceRes experience) {
        }
}
