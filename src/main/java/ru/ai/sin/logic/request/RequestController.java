package ru.ai.sin.logic.request;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.request.dto.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/request")
@Tag(name = "Request", description = "Операции управления заявками")
public class RequestController {

    private final RequestService requestService;

    @Operation(summary = "Получить заявку по ID", description = "Возвращает детальную информацию о заявке")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable @Min(1) long id) {
        return ResponseEntity.ok(requestService.getById(id));
    }

    @Operation(summary = "Фильтр заявок", description = "Принимает DTO фильтра в request body и Pageable без параметра sort")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<RequestDTO>> getByFilter(
            @PageableDefault Pageable pageable,
            @Valid @RequestBody FilterRequestReq filterRequestReq) {
        return ResponseEntity.ok(requestService.getByFilter(pageable, filterRequestReq));
    }

    @Operation(
            summary = "Создать заявку",
            description = "Заявка от рекрутера на студента. После первой заявки с полными данными профиль рекрутера привязывается к пользователю; "
                    + "далее достаточно studentId (проверка: GET /recruiter/me).")
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<RequestDTO> create(@Valid @RequestBody AddRequestReq addRequestReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.create(addRequestReq));
    }

    @Operation(summary = "Удалить заявку", description = "Удаляет заявку по ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        requestService.deleteById(id);
    }
}
