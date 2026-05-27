package ru.ai.sin.logic.siteproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(
        name = "CreateSiteProjectReq",
        description = "Тело создания проекта для ленты (`POST /admin/projects`), только ADMIN.")
public record CreateSiteProjectReq(
        @NotBlank @Size(max = 255)
        @Schema(description = "Заголовок карточки проекта")
        String title,
        @Schema(description = "Краткое описание (подзаголовок), может быть null")
        String summary,
        @Schema(description = "Полный текст / HTML по соглашению фронта, может быть null")
        String body,
        @Size(max = 512)
        @Schema(description = "Ключ или путь к изображению в хранилище приложения, может быть null")
        String imagePath,
        @Schema(description = "Показывать на `GET /public/projects`; если false — только на `GET /projects` для авторизованных (плюс окно публикации)")
        boolean visibleToAnonymous,
        @Schema(description = "Нижняя граница публикации; null — без ограничения «не раньше»")
        LocalDateTime publishedFrom,
        @Schema(description = "Верхняя граница публикации; null — без ограничения «не позже»")
        LocalDateTime publishedTo
) {
}
