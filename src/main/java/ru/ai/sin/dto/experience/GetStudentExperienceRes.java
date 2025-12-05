package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.dto.company.CompanyRes;

import java.util.UUID;

public record GetStudentExperienceRes(
        @NotNull
        CompanyRes company,

        @NotNull
        ExperienceRes experience) {
}
