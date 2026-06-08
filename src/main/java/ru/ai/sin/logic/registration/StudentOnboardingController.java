package ru.ai.sin.logic.registration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.ai.sin.logic.portfolio.dto.PortfolioDTO;
import ru.ai.sin.logic.registration.dto.OnboardingStatusRes;
import ru.ai.sin.logic.registration.dto.StudentPortfolioItemReq;
import ru.ai.sin.logic.registration.dto.StudentResumeEditRes;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.student.dto.StudentDTO;

import java.util.List;

@RestController
@RequestMapping("/student/onboarding")
@RequiredArgsConstructor
@Tag(name = "StudentOnboarding", description = "Онбординг резюме после регистрации студента")
public class StudentOnboardingController {

    private final StudentResumeOnboardingService studentResumeOnboardingService;

    @Operation(summary = "Создать резюме после регистрации", description = "Только STUDENT без привязанной карточки.")
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/resume")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StudentDTO> completeResume(@Valid @RequestBody StudentResumeOnboardingReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentResumeOnboardingService.completeResume(req));
    }

    @Operation(summary = "Получить резюме для редактирования", description = "Только STUDENT с привязанной карточкой.")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/resume")
    public ResponseEntity<StudentResumeEditRes> getResumeForEdit() {
        return ResponseEntity.ok(studentResumeOnboardingService.getResumeForEdit());
    }

    @Operation(summary = "Обновить резюме", description = "Только STUDENT с привязанной карточкой. Новые записи опыта и образования добавляются к существующим.")
    @PreAuthorize("hasRole('STUDENT')")
    @PutMapping("/resume")
    public ResponseEntity<StudentDTO> updateResume(@Valid @RequestBody StudentResumeOnboardingReq req) {
        return ResponseEntity.ok(studentResumeOnboardingService.updateResume(req));
    }

    @Operation(summary = "Статус онбординга резюме")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/status")
    public ResponseEntity<OnboardingStatusRes> status() {
        return ResponseEntity.ok(new OnboardingStatusRes(studentResumeOnboardingService.hasResumeForCurrentUser()));
    }

    @Operation(summary = "Список портфолио текущего студента")
    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/portfolio")
    public ResponseEntity<List<PortfolioDTO>> listPortfolio() {
        return ResponseEntity.ok(studentResumeOnboardingService.listPortfoliosForCurrentUser());
    }

    @Operation(summary = "Добавить запись в портфолио")
    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/portfolio")
    public ResponseEntity<PortfolioDTO> addPortfolio(@Valid @RequestBody StudentPortfolioItemReq req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentResumeOnboardingService.addPortfolio(req));
    }

    @Operation(summary = "Удалить запись портфолио")
    @PreAuthorize("hasRole('STUDENT')")
    @DeleteMapping("/portfolio/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePortfolio(@PathVariable long id) {
        studentResumeOnboardingService.deletePortfolio(id);
    }
}
