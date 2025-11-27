package ru.ai.sin.dto.education;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.ai.sin.dto.skill.SkillDTO;

import java.util.List;
import java.util.UUID;

public record EducationDTO(
        long id,

        @NotBlank
        @Size(min = 1, max = 255, message = "Institution must be less than 2000 characters")
        String institution,

        @NotBlank
        @Size(min = 1, max = 2000, message = "Additional info must be less than 2000 characters")
        String additionalInfo,

        @NotNull
        List<UUID> studentIds,

        @NotNull
        List<SkillDTO> skills) {
}
