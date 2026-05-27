package ru.ai.sin.logic.siteproject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;

import java.util.List;

@RestController
@RequestMapping("/public/projects")
@RequiredArgsConstructor
@Tag(
        name = "PublicProjects",
        description = """
                Публичное чтение ленты проектов сайта **без авторизации**.
                Возвращаются только записи с `visibleToAnonymous=true`, попадающие в окно публикации.
                Для вошедших студентов и рекрутеров — `GET /projects` (все опубликованные, включая только для авторизованных).""")
public class PublicSiteProjectController {

    private final SiteProjectService siteProjectService;

    @Operation(
            summary = "Список проектов для витрины",
            description = """
                    **200** — массив `SiteProjectDTO` (может быть пустым, если нет подходящих записей).

                    Поля `summary` / `body` / `imagePath` зависят от данных, заведённых админом в `POST/PUT /admin/projects`.

                    Аутентификация не требуется.""")
    @GetMapping
    public ResponseEntity<List<SiteProjectDTO>> list() {
        return ResponseEntity.ok(siteProjectService.listPublicVisible());
    }
}
