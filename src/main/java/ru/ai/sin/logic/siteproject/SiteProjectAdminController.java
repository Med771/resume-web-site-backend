package ru.ai.sin.logic.siteproject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.logic.siteproject.dto.CreateSiteProjectReq;
import ru.ai.sin.logic.siteproject.dto.ReorderSiteProjectsReq;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;
import ru.ai.sin.logic.siteproject.dto.UpdateSiteProjectReq;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/site-projects")
@RequiredArgsConstructor
@Tag(
        name = "AdminSiteProjects",
        description = """
                CRUD и сортировка ленты проектов на сайте. Доступно **только роли ADMIN** (JWT в cookie).
                Публичная выдача — отдельно в `GET /public/projects` с фильтром `visibleToAnonymous` и окнами публикации.""")
@PreAuthorize("hasRole('ADMIN')")
public class SiteProjectAdminController {

    private final SiteProjectService siteProjectService;

    @Operation(
            summary = "Список всех проектов (админ)",
            description = """
                    Все записи таблицы в порядке `sortOrder` (как настроено реордером).
                    Отличается от публичного списка: здесь нет фильтра `visibleToAnonymous` и дат публикации.

                    **401/403** — нет или недостаточно прав.""")
    @GetMapping
    public ResponseEntity<List<SiteProjectDTO>> list() {
        return ResponseEntity.ok(siteProjectService.listAdminOrdered());
    }

    @Operation(
            summary = "Создать проект",
            description = "Создаёт запись; `sortOrder` назначается сервером (в конец очереди). **201** + тело созданного DTO.")
    @PostMapping
    public ResponseEntity<SiteProjectDTO> create(@Valid @RequestBody CreateSiteProjectReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(siteProjectService.create(req));
    }

    @Operation(
            summary = "Обновить проект",
            description = "Полная замена полей по `id`. **404**, если проект не найден.")
    @PutMapping("/{id}")
    public ResponseEntity<SiteProjectDTO> update(
            @Parameter(description = "UUID проекта", required = true) @PathVariable UUID id,
            @Valid @RequestBody UpdateSiteProjectReq req) {
        return ResponseEntity.ok(siteProjectService.update(id, req));
    }

    @Operation(summary = "Удалить проект", description = "**204** при успехе. **404**, если `id` не существует.")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@Parameter(description = "UUID проекта", required = true) @PathVariable UUID id) {
        siteProjectService.delete(id);
    }

    @Operation(
            summary = "Задать порядок проектов",
            description = """
                    Тело: `orderedIds` — список UUID проектов в желаемом порядке: элемент с индексом `i` получит `sortOrder = i`.
                    Каждый id должен существовать; дубликаты в списке запрещены (**400**). Неупомянутые в списке проекты **не** меняют порядок автоматически.

                    **204** при успехе.""")
    @PostMapping("/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorder(@Valid @RequestBody ReorderSiteProjectsReq req) {
        siteProjectService.reorder(req);
    }
}
