package ru.ai.sin.logic.sync;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.logic.sync.dto.SyncDTO;
import ru.ai.sin.logic.sync.dto.SyncUpdateReq;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/sync")
@Tag(name = "Sync", description = "Операции синхронизации внешних данных")
public class SyncController {

    private final SyncService syncService;

    @Operation(summary = "Получить sync по userId", description = "Возвращает состояние синхронизации по идентификатору пользователя")
    @GetMapping(path = "/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SyncDTO> getByUserId(
            @PathVariable @NotBlank String userId) {
        return ResponseEntity.ok(syncService.getByUserId(userId));
    }

    @Operation(summary = "Обновить sync", description = "Обновляет состояние синхронизации по ID")
    @PostMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(
            @PathVariable UUID id,
            @Valid @RequestBody SyncUpdateReq req) {
        syncService.update(id, req);
    }
}
