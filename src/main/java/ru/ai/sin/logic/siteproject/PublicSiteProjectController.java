package ru.ai.sin.logic.siteproject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/public/projects")
@RequiredArgsConstructor
@Tag(
        name = "PublicProjects",
        description = """
                Публичное чтение ленты проектов сайта **без авторизации**.
                Возвращаются только записи с `visibleToAnonymous=true`, попадающие в окно публикации.
                Список участников (`students`) не передаётся.""")
public class PublicSiteProjectController {

    private final SiteProjectService siteProjectService;

    @Operation(
            summary = "Список проектов для витрины",
            description = """
                    **200** — массив `SiteProjectDTO` (может быть пустым).

                    Аутентификация не требуется. Поле `students` отсутствует.""")
    @GetMapping
    public ResponseEntity<List<SiteProjectDTO>> list(
            @Parameter(description = "Поиск по названию, описанию, тексту и разделу")
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(siteProjectService.listPublicVisible(q));
    }

    @Operation(
            summary = "Проект для витрины по id",
            description = """
                    **200** — `SiteProjectDTO` без участников.
                    **404** — проект скрыт от анонимов или вне окна публикации.""")
    @GetMapping("/{id}")
    public ResponseEntity<SiteProjectDTO> getById(
            @Parameter(description = "UUID проекта", required = true) @PathVariable UUID id) {
        return ResponseEntity.ok(siteProjectService.getPublicVisibleById(id));
    }
}
