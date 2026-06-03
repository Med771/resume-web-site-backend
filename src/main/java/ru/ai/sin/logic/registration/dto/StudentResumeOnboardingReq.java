package ru.ai.sin.logic.registration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import ru.ai.sin.logic.student.dto.CreateStudentExperienceReq;
import ru.ai.sin.logic.student.dto.CreateStudentInstitutionReq;
import ru.ai.sin.models.enums.BusynessEnum;

import java.time.LocalDate;
import java.util.List;

@Schema(name = "StudentResumeOnboardingReq", description = "Заполнение резюме после регистрации аккаунта студента. Курс на сервере всегда NEW.")
public record StudentResumeOnboardingReq(
        @Size(min = 1, max = 255)
        String city,

        @Size(min = 1, max = 255)
        String hhLink,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @Size(max = 2000)
        String bio,

        @NotNull
        BusynessEnum busyness,

        @NotBlank
        @Size(max = 255)
        String firstName,

        @NotBlank
        @Size(max = 255)
        String lastName,

        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @Pattern(regexp = "\\+?\\d{1,32}", message = "Phone number must contain 1-32 digits and optional + at start")
        String phoneNumber,

        @Size(min = 1, max = 255)
        String telegramUsername,

        @Positive
        long specialityId,

        List<@Positive Long> skillsIds,

        List<@Valid CreateStudentExperienceReq> experiences,

        List<@Valid CreateStudentInstitutionReq> institutions
) {
}
