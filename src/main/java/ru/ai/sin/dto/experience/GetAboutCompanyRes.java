package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.CompanyEnt;

import java.util.List;

public record GetAboutCompanyRes(
        @NotNull
        CompanyEnt company,

        @NotNull
        List<GetCompanyExperienceRes> studentsExperiences) {
}
