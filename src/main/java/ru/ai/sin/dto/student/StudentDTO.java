package ru.ai.sin.dto.student;

import jakarta.validation.constraints.*;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.model.BusynessEnum;
import ru.ai.sin.entity.model.CourseEnum;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StudentDTO(
        @NotNull
        UUID id,

        @Size(min = 1, max = 255, message = "City must be less than 255 characters")
        String city,

        @Size(min = 1, max = 255, message = "City must be less than 255 characters")
        String hhLink,

        @NotNull
        LocalDate birthDate,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String bio,

        String imagePath,

        @NotNull
        CourseEnum course,

        @NotNull
        BusynessEnum busyness,

        String firstName,
        String lastName,

        @NotNull
        String speciality,

        @NotNull
        List<SkillDTO> skills) {
}
