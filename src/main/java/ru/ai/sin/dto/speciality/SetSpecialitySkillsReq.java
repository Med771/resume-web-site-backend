package ru.ai.sin.dto.speciality;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record SetSpecialitySkillsReq(
        @NotNull
        List<Long> skillsIds) {
}
