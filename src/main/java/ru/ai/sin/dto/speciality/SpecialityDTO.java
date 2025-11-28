package ru.ai.sin.dto.speciality;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import ru.ai.sin.dto.skill.SkillDTO;

import java.time.LocalDateTime;
import java.util.List;

public record SpecialityDTO(
        long id,

        @NotBlank
        @Size(min = 1, max = 255, message = "Name must be less than 255 characters")
        String name,

        @NotNull
        List<SkillDTO> skills) {
}
