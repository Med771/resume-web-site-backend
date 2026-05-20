package ru.ai.sin.logic.siteproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(
        name = "SiteProjectDTO",
        description = "Проект ленты: ответ админских и публичного API (набор полей одинаковый; публичный список фильтруется на сервере).")
public record SiteProjectDTO(
        @Schema(description = "UUID записи", requiredMode = Schema.RequiredMode.REQUIRED)
        UUID id,
        @Schema(description = "Заголовок")
        String title,
        @Schema(description = "Краткое описание")
        String summary,
        @Schema(description = "Полный текст")
        String body,
        @Schema(description = "Путь/ключ изображения")
        String imagePath,
        @Schema(description = "Порядок сортировки (меньше — выше в списке при одинаковых условиях фильтра)")
        int sortOrder,
        @Schema(description = "Доступен ли на `GET /public/projects` при прочих условиях")
        boolean visibleToAnonymous,
        @Schema(description = "Начало окна публикации; null — без нижней границы")
        LocalDateTime publishedFrom,
        @Schema(description = "Конец окна публикации; null — без верхней границы")
        LocalDateTime publishedTo
) {
}
