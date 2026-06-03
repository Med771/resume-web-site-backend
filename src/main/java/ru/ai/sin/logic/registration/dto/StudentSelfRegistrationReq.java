package ru.ai.sin.logic.registration.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import ru.ai.sin.logic.student.dto.CreateStudentExperienceReq;
import ru.ai.sin.logic.student.dto.CreateStudentInstitutionReq;
import ru.ai.sin.models.enums.BusynessEnum;

import java.time.LocalDate;
import java.util.List;

/**
 * Саморегистрация студента: учётная запись + анкета. Курс обучения на сервере всегда выставляется в NEW
 * (карточка не видна рекрутерам до модерации администратором).
 */
@Schema(name = "StudentSelfRegistrationReq", description = "Регистрация студента: логин, пароль и профиль")
public record StudentSelfRegistrationReq(
        @Schema(description = "Логин (совпадает с правилами пользователя)")
        @NotBlank
        @Size(min = 3, max = 64)
        @Pattern(regexp = "^[a-zA-Z0-9_]{3,64}$", message = "Username must be 3-64 characters, letters, digits or _")
        String username,

        @Schema(description = "Пароль")
        @NotBlank
        String password,

        @Schema(description = "Подтверждение пароля")
        @NotBlank
        String passwordConfirm,

        @Schema(description = "Отображаемое имя в системе (необязательно)")
        @Size(max = 255)
        String name,

        @Schema(description = "Город")
        @Size(min = 1, max = 255)
        String city,

        @Schema(description = "Ссылка на HH")
        @Size(min = 1, max = 255)
        String hhLink,

        @Schema(description = "Дата рождения")
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        @Past(message = "Birth date must be in the past")
        LocalDate birthDate,

        @Schema(description = "О себе")
        @Size(max = 2000)
        String bio,

        @Schema(description = "Занятость")
        @NotNull
        BusynessEnum busyness,

        @Schema(description = "Имя")
        @NotBlank
        @Size(max = 255)
        String firstName,

        @Schema(description = "Фамилия")
        @NotBlank
        @Size(max = 255)
        String lastName,

        @Schema(description = "Email (уникален среди анкет)")
        @NotBlank
        @Email
        @Size(max = 255)
        String email,

        @Schema(description = "Телефон")
        @Pattern(regexp = "\\+?\\d{1,32}", message = "Phone number must contain 1-32 digits and optional + at start")
        String phoneNumber,

        @Schema(description = "Telegram username")
        @Size(min = 1, max = 255)
        String telegramUsername,

        @Schema(description = "ID специальности из справочника")
        @Positive
        long specialityId,

        @Schema(description = "ID навыков (необязательно, не более лимита из конфигурации)")
        List<@Positive Long> skillsIds,

        @Schema(description = "Опыт: либо companyId из GET /public/registration/companies, либо своё название companyName")
        List<@Valid CreateStudentExperienceReq> experiences,

        @Schema(description = "Образование: либо educationId из GET /public/registration/educations, либо своё institution + webUrl")
        List<@Valid CreateStudentInstitutionReq> institutions
) {
}
