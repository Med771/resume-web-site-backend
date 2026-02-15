package ru.ai.sin.logic.telegram;

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
public class TelegramController {

    private final TelegramService telegramService;

    @PutMapping(path = "/{id}")
    public ResponseEntity<RequestDTO> updateRequest(
            @PathVariable @Min(1) long id,
            @Valid @RequestBody UpdateRequestTelegramReq req) {
        return ResponseEntity.ok(telegramService.updateRequest(id, req));
    }
}
