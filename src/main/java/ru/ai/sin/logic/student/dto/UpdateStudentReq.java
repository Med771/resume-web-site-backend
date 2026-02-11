package ru.ai.sin.logic.student.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.models.enums.CourseEnum;

import java.time.LocalDate;
import java.util.List;

public record UpdateStudentReq(
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

        @NotNull
        List<@Positive Long> skillsIds) {
}
