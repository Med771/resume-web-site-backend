package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record GetAboutStudentRes(
        @NotNull
        UUID studentId,

        @NotNull
        List<GetStudentExperienceRes> companyExperiences) {
}
