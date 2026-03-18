package ru.ai.sin.logic.student.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreateStudentSkillReq(
        @Positive
        Long id,

        @Size(min = 1, max = 255, message = "Skill name must be less than 255 characters")
        String name
) {
}
