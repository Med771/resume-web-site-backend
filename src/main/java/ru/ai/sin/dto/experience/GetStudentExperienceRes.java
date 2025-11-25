package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.CompanyEnt;

import java.util.List;

public record GetStudentExperienceRes(
        @NotNull
        CompanyEnt company,

        @NotNull
        List<ExperienceRes> companyExperiences) {
}
