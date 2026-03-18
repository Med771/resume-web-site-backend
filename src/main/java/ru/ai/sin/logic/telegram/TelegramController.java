package ru.ai.sin.logic.telegram;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.logic.request.dto.RequestDTO;
import ru.ai.sin.logic.telegram.dto.UpdateRequestTelegramReq;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/telegram")
@Tag(name = "Telegram", description = "Интеграционные операции Telegram")
public class TelegramController {

    private final TelegramService telegramService;

    @Operation(summary = "Обновить заявку из Telegram", description = "Обновляет поля заявки на основе входящего события Telegram")
    @PutMapping(path = "/{id}")
    public ResponseEntity<RequestDTO> updateRequest(
            @PathVariable @Min(1) long id,
            @Valid @RequestBody UpdateRequestTelegramReq req) {
        return ResponseEntity.ok(telegramService.updateRequest(id, req));
    }
}
