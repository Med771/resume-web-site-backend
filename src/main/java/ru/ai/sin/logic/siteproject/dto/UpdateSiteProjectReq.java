package ru.ai.sin.logic.siteproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Schema(
        name = "UpdateSiteProjectReq",
        description = "Полная замена полей проекта (`PUT /admin/projects/{id}`), только ADMIN.")
public record UpdateSiteProjectReq(
        @NotBlank @Size(max = 255)
        @Schema(description = "Заголовок")
        String title,
        @Schema(description = "Краткое описание")
        String summary,
        @Schema(description = "Полный текст")
        String body,
        @Size(max = 512)
        @Schema(description = "Изображение")
        String imagePath,
        @Schema(description = "Видимость анонимам на публичной ленте")
        boolean visibleToAnonymous,
        @Schema(description = "Начало окна публикации")
        LocalDateTime publishedFrom,
        @Schema(description = "Конец окна публикации")
        LocalDateTime publishedTo
) {
}
