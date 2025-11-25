package ru.ai.sin.dto.experience;

import jakarta.validation.constraints.NotNull;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;

public record GetCompanyExperienceRes(

        @NotNull
        StudentEnt student,

        @NotNull
        List<ExperienceRes> studentExperiences) {
}
