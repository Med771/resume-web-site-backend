package ru.ai.sin.controller;

import jakarta.validation.Valid;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.telegram.*;

import ru.ai.sin.service.impl.TelegramService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/telegram")
public class TelegramController {

    private final TelegramService telegramService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/user/{telegramUserId}")
    public ResponseEntity<TelegramUserRes> getByTelegramUserId(@PathVariable String telegramUserId) {
        return ResponseEntity.ok(telegramService.getByTelegramUserId(telegramUserId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<OffersDTO.Offer> getOfferById(@PathVariable long id) {
        return ResponseEntity.ok(telegramService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/offers")
    public ResponseEntity<OffersDTO> getAllOffersByResult(@RequestBody @Valid OffersFilterReq offersFilterReq) {
        return ResponseEntity.ok(telegramService.getAllOffersByResult(offersFilterReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/query/{userId}")
    public ResponseEntity<OffersDTO> filter(
            @PathVariable @NonNull String userId,
            @RequestBody @Valid OfferFilterReq offerFilterReq) {
        return ResponseEntity.ok(telegramService.filter(userId, offerFilterReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/chat/{id}")
    public ResponseEntity<OffersDTO.Offer> createChat(@PathVariable long id) {
        return ResponseEntity.ok(telegramService.createChat(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/batchStatus")
    public ResponseEntity<OffersDTO> batchStatus(@RequestBody @Valid StatusUpdateReq statusUpdateReq) {
        return ResponseEntity.ok(telegramService.batchStatus(statusUpdateReq));
    }
}

