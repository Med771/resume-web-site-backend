package ru.ai.sin.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.ai.sin.dto.skill.SkillDTO;

import java.time.LocalDateTime;
import java.util.List;

public record GetEducationRes(
        long id,

        @NotBlank
        @Size(min = 1, max = 255, message = "Institution must be less than 2000 characters")
        String institution,

        @NotBlank
        @Size(min = 1, max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        @NotNull
        LocalDateTime createdAt,
        @NotNull
        LocalDateTime updatedAt,

        @NotNull
        List<SkillDTO> skills) {
}
