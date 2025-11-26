package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;

public record GetAboutStudentRes(
        @NotNull
        StudentEnt student,

        @NotNull
        List<GetStudentExperienceRes> companyExperiences) {
}
