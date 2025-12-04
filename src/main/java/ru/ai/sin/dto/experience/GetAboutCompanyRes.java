package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record GetAboutCompanyRes(
        @NotNull
        long companyId,

        @NotNull
        List<GetCompanyExperienceRes> studentsExperiences) {
}
