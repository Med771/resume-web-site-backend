package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.entity.StudentEnt;

public record GetExperienceRes(
        @NotNull
        CompanyEnt company,

        @NotNull
        StudentEnt student,

        @NotNull
        ExperienceRes experience) {
}
