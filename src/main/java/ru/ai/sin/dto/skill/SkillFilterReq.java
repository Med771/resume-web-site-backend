package ru.ai.sin.dto.skill;

import jakarta.validation.constraints.Size;

public record SkillFilterReq(
        @Size(min = 1, max = 255, message = "Add skill name must be less than 255 characters")
        String name) {
}
