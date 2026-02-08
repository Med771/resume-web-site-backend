package ru.ai.sin.logic.student.dto;

import jakarta.validation.constraints.Positive;
import ru.ai.sin.entity.model.BusynessEnum;
import ru.ai.sin.entity.model.CourseEnum;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;

public record FilterStudentReq(
        String findString,

        Set<CourseEnum> course,

        Set<BusynessEnum> busyness,

        LocalDate bornBefore,

        LocalDate bornAfter,

        List<@Positive Long> skillsIds,

        List<@Positive Long> specialitiesIds) {
}
