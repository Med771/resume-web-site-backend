package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record GetStudentExperienceRes(
        @NotNull
        UUID companyId,

        @NotNull
        ExperienceRes experience) {
}
