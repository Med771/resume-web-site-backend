package ru.ai.sin.dto.student;

import jakarta.validation.constraints.Positive;
import ru.ai.sin.entity.model.BusynessEnum;
import ru.ai.sin.entity.model.CourseEnum;

import java.time.LocalDate;

import java.util.List;

public record GetStudentFilterReq(
        CourseEnum course,

        BusynessEnum busyness,

        LocalDate bornBefore,

        LocalDate bornAfter,

        List<@Positive Long> skillsIds,

        List<@Positive Long> specialitiesIds) {
}
