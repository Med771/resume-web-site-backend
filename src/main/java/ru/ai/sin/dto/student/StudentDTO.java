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

        @Pattern(regexp = "^[a-zA-Z0-9_]{3,64}$", message = "Username must be 3-64 characters, letters, digits or _")
        String username,

        @Email(message = "Email should be valid")
        String email,

        @Pattern(regexp = "\\+?\\d{1,32}", message = "Phone number must contain 1-32 digits and optional + at start")
        String phoneNumber,

        @Size(min = 1, max = 255, message = "Telegram Username must be less than 255 characters")
        String telegramUsername,

        @NotNull
        String speciality,

        @NotNull
        List<SkillDTO> skills) {
}
