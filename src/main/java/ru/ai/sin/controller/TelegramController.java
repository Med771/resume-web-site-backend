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

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/telegram")
public class TelegramController {

    private final TelegramService telegramService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/student/{id}")
    public ResponseEntity<StudentTelegramDTO> getStudent(@PathVariable String id) {
        StudentTelegramDTO studentTelegramDTO = telegramService.getStudentByTelegramUserId(id);

        return ResponseEntity.ok(studentTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/recruiter/{id}")
    public ResponseEntity<RecruiterTelegramDTO> getRecruiter(@PathVariable String id) {
        RecruiterTelegramDTO recruiterTelegramDTO = telegramService.getRecruiterByTelegramUserId(id);

        return ResponseEntity.ok(recruiterTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<OffersDTO.Offer> filter(
            @PathVariable long id
    ) {
        OffersDTO.Offer offerDTO = telegramService.getById(id);

        return ResponseEntity.ok(offerDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/query/{userId}")
    public ResponseEntity<OffersDTO> filter(
            @PathVariable @NonNull String userId,
            @RequestBody @Valid OfferFilterReq offerFilterReq
            ) {
        OffersDTO offersDTO = telegramService.filter(userId, offerFilterReq);

        return ResponseEntity.ok(offersDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/student/{id}")
    public ResponseEntity<StudentTelegramDTO> setStudentTelegramUserId(
            @PathVariable UUID id,
            @Valid @RequestBody SetTelegramUserIdReq setTelegramUserIdReq
    ) {
        StudentTelegramDTO studentTelegramDTO = telegramService.setStudentTelegramUserId(id, setTelegramUserIdReq);

        return ResponseEntity.ok(studentTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/recruiter/{id}")
    public ResponseEntity<RecruiterTelegramDTO> setRecruiterTelegramUserId(
            @PathVariable UUID id,
            @Valid @RequestBody SetTelegramUserIdReq setTelegramUserIdReq
    ) {
        RecruiterTelegramDTO recruiterTelegramDTO = telegramService.setRecruiterTelegramUserId(id, setTelegramUserIdReq);

        return ResponseEntity.ok(recruiterTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/chat/{id}")
    public ResponseEntity<OffersDTO.Offer> createChat(@PathVariable long id) {
        OffersDTO.Offer offer = telegramService.createChat(id);

        return ResponseEntity.ok(offer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/student/{id}")
    public ResponseEntity<StudentTelegramDTO> clearStudentTelegramUserId(
            @PathVariable UUID id
    ) {
        StudentTelegramDTO studentTelegramDTO = telegramService.clearStudentTelegramUserId(id);

        return ResponseEntity.ok(studentTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/recruiter/{id}")
    public ResponseEntity<RecruiterTelegramDTO> clearRecruiterTelegramUserId(
            @PathVariable UUID id
    ) {
        RecruiterTelegramDTO recruiterTelegramDTO = telegramService.clearRecruiterTelegramUserId(id);

        return ResponseEntity.ok(recruiterTelegramDTO);
    }
}

