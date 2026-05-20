package ru.ai.sin.logic.analytics;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ai.sin.logic.analytics.dto.AnalyticsSummaryDTO;
import ru.ai.sin.logic.analytics.dto.AnalyticsSummaryReq;

@RestController
@RequestMapping("/admin/analytics")
@RequiredArgsConstructor
@Tag(
        name = "AdminAnalytics",
        description = """
                Агрегированные отчёты по сохранённым событиям (`/public/analytics/events`).
                Доступно **только ADMIN** (JWT в cookie).""")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(
            summary = "Сводка по путям за интервал времени",
            description = """
                    Возвращает список пар (`path`, `events`) — число событий по каждому `path` за полуинтервал **[from, to)**.

                    **200** — `AnalyticsSummaryDTO` с полем `byPath`.

                    **400** — `from` / `to` null или нарушение валидации.

                    **401/403** — нет входа или не админ.""")
    @PostMapping("/summary")
    public ResponseEntity<AnalyticsSummaryDTO> summary(@Valid @RequestBody AnalyticsSummaryReq req) {
        return ResponseEntity.ok(analyticsService.summarize(req));
    }
}
