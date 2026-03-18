package ru.ai.sin.logic.student.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.models.enums.CourseEnum;

import java.time.LocalDate;
import java.util.List;

public record CreateStudentExtendedReq(
        @Size(min = 1, max = 255, message = "City must be less than 255 characters")
        String city,

        @Size(min = 1, max = 255, message = "HH link must be less than 255 characters")
        String hhLink,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @Size(max = 2000, message = "Additional info must be less than 2000 characters")
        String bio,

        @NotNull
        CourseEnum course,

        @NotNull
        BusynessEnum busyness,

        @NotBlank
        String firstName,

        @NotBlank
        String lastName,

        @Email(message = "Email should be valid")
        String email,

        @Pattern(regexp = "\\+?\\d{1,32}", message = "Phone number must contain 1-32 digits and optional + at start")
        String phoneNumber,

        @Size(min = 1, max = 255, message = "Telegram Username must be less than 255 characters")
        String telegramUsername,

        @Positive
        long specialityId,

        List<@Positive Long> skillsIds,
        List<@Valid CreateStudentSkillReq> skills,
        List<@Valid CreateStudentPortfolioReq> portfolio,
        List<@Valid CreateStudentExperienceReq> experiences,
        List<@Valid CreateStudentInstitutionReq> institutions
) {
}
