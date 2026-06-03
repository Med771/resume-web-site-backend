package ru.ai.sin.logic.siteproject;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Tag(
        name = "Projects",
        description = """
                Витрина проектов для **авторизованных** пользователей (STUDENT, RECRUITER, ADMIN).
                Рекрутер и админ видят участников проекта; студент — только описание.
                Анонимная витрина — `GET /public/projects`.""")
public class SiteProjectController {

    private static final Set<String> ROLES_WITH_STUDENTS = Set.of("ROLE_RECRUITER", "ROLE_ADMIN");

    private final SiteProjectService siteProjectService;

    @Operation(
            summary = "Список проектов для авторизованных",
            description = """
                    **200** — массив `SiteProjectDTO` в порядке `sortOrder`.

                    **RECRUITER** / **ADMIN** — с полем `students`. **STUDENT** — без участников.
                    **401** без входа. Полный CRUD — `GET /admin/projects`.""")
    @GetMapping
    @PreAuthorize("hasAnyRole('STUDENT', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<List<SiteProjectDTO>> list(
            Authentication authentication,
            @Parameter(description = "Поиск по названию, описанию, тексту и разделу")
            @RequestParam(required = false) String q) {
        return ResponseEntity.ok(siteProjectService.listAuthenticatedVisible(includeStudents(authentication), q));
    }

    @Operation(
            summary = "Проект по id для авторизованных",
            description = """
                    **200** — `SiteProjectDTO`. Участники для **RECRUITER** и **ADMIN**.
                    **404** — проект вне окна публикации.""")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STUDENT', 'RECRUITER', 'ADMIN')")
    public ResponseEntity<SiteProjectDTO> getById(
            @Parameter(description = "UUID проекта", required = true) @PathVariable UUID id,
            Authentication authentication) {
        return ResponseEntity.ok(
                siteProjectService.getAuthenticatedVisibleById(id, includeStudents(authentication)));
    }

    private static boolean includeStudents(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(ROLES_WITH_STUDENTS::contains);
    }
}
