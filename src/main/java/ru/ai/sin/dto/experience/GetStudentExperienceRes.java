package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.CompanyEnt;

import java.util.List;
import java.util.UUID;

public record GetStudentExperienceRes(
        @NotNull
        UUID companyId,

        @NotNull
        List<ExperienceRes> companyExperiences) {
}
