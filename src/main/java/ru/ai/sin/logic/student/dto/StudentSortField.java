package ru.ai.sin.logic.student.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        enumAsRef = true,
        description = """
                Разрешённые ключи сортировки для `FilterStudentReq`.
                Произвольные имена полей из query `sort=` **не** поддерживаются.""")
public enum StudentSortField {
    @Schema(description = "Релевантность: аватар (непустой `imagePath` выше), затем `profileTextScore` DESC, затем дата создания DESC")
    RELEVANCE,
    @Schema(description = "Время создания записи (`timestamps.createdAt`)")
    CREATED_AT,
    @Schema(description = "Фамилия (`userInformation.lastName`)")
    LAST_NAME,
    @Schema(description = "Дата рождения")
    BIRTH_DATE,
    @Schema(description = "Денормализованная сумма длин текстовых полей профиля (обновляется при сохранении)")
    PROFILE_TEXT_SCORE
}
