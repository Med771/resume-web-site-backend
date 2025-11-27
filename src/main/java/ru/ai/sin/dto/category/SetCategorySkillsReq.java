package ru.ai.sin.dto.category;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SetCategorySkillsReq(
        @NotNull
        List<Long> skillsIds) {
}
