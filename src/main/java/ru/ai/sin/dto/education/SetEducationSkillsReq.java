package ru.ai.sin.dto.education;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SetEducationSkillsReq(
        @NotNull
        List<Long> skillsIds) {
}
