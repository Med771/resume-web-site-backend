package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record GetCompanyExperienceRes(
        @NotNull
        UUID studentId,

        @NotNull
        ExperienceRes experience) {
}
