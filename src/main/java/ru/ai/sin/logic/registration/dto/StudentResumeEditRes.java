package ru.ai.sin.logic.registration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.models.enums.CourseEnum;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "StudentResumeEditRes", description = "Данные резюме текущего студента для редактирования")
public record StudentResumeEditRes(
        String city,
        String hhLink,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate birthDate,
        String bio,
        BusynessEnum busyness,
        CourseEnum course,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String telegramUsername,
        long specialityId,
        List<Long> skillsIds,
        List<StudentResumeExperienceItem> experiences,
        List<StudentResumeInstitutionItem> institutions
) {
    @Schema(name = "StudentResumeExperienceItem")
    public record StudentResumeExperienceItem(
            long id,
            String companyName,
            String position,
            String additionalInfo,
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate startDate,
            @JsonFormat(pattern = "yyyy-MM-dd")
            LocalDate endDate
    ) {
    }

    @Schema(name = "StudentResumeInstitutionItem")
    public record StudentResumeInstitutionItem(
            long id,
            String institution,
            String webUrl,
            String additionalInfo,
            int startYear,
            int endYear
    ) {
    }
}
