package ru.ai.sin.logic.siteproject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(
        name = "Projects",
        description = """
                Витрина проектов для **авторизованных** пользователей (STUDENT, GUEST/рекрутер, USER).
                Все проекты в окне публикации, в том числе с `visibleToAnonymous=false`.
                Анонимная витрина — `GET /public/projects` (только `visibleToAnonymous=true`).""")
public class SiteProjectController {

    private final SiteProjectService siteProjectService;

    @Operation(
            summary = "Список проектов для авторизованных",
            description = """
                    **200** — массив `SiteProjectDTO` в порядке `sortOrder`.

                    Требуется JWT в cookie. Роли: **STUDENT**, **GUEST** (рекрутер), **USER**.
                    **401** без входа, **403** для роли **ADMIN** (полный список — `GET /admin/projects`).""")
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'GUEST', 'USER')")
    public ResponseEntity<List<SiteProjectDTO>> list() {
        return ResponseEntity.ok(siteProjectService.listAuthenticatedVisible());
    }
}
