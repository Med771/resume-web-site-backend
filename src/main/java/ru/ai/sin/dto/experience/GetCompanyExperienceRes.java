package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;
import java.util.UUID;

public record GetCompanyExperienceRes(
        @NotNull
        UUID studentId,

        @NotNull
        List<ExperienceRes> studentExperiences) {
}
