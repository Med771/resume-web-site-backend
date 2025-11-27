package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.entity.StudentEnt;

import java.util.UUID;

public record ExperienceDTO(
        @NotNull
        UUID companyId,

        @NotNull
        UUID studentId,

        @NotNull
        ExperienceRes experience) {
}
