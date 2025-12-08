package ru.ai.sin.dto.speciality;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record SetSpecialitySkillsReq(
        @NotNull
        List<@Positive Long> skillsIds) {
}
