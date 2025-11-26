package ru.ai.sin.dto.student;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.model.CourseEnum;

import java.util.List;
import java.util.UUID;

public record StudentCardDTO(
        @NotNull
        UUID id,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String bio,

        String firstName,
        String lastName,

        String imagePath,

        @NotNull
        CourseEnum course,

        @NotNull
        String speciality,

        @NotNull
        List<SkillDTO> skills) {
}
