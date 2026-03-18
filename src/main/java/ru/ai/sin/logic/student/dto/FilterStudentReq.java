package ru.ai.sin.logic.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.models.enums.CourseEnum;

import java.time.LocalDate;

import java.util.List;
import java.util.Set;

@Schema(name = "FilterStudentReq", description = "DTO фильтрации студентов")
public record FilterStudentReq(
        @Schema(description = "Поисковая строка по ФИО/полям студента")
        String findString,

        @Schema(description = "Фильтр по курсам")
        Set<CourseEnum> course,

        @Schema(description = "Фильтр по занятости")
        Set<BusynessEnum> busyness,

        @Schema(description = "Родился до даты")
        LocalDate bornBefore,

        @Schema(description = "Родился после даты")
        LocalDate bornAfter,

        @Schema(description = "Список ID навыков")
        List<@Positive Long> skillsIds,

        @Schema(description = "Список ID специальностей")
        List<@Positive Long> specialitiesIds) {
}
