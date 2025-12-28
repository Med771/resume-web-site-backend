package ru.ai.sin.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.telegram.RecruiterTelegramDTO;
import ru.ai.sin.dto.telegram.SetTelegramUserIdReq;
import ru.ai.sin.dto.telegram.StudentTelegramDTO;
import ru.ai.sin.service.impl.TelegramService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/telegram")
public class TelegramCnt {

    private final TelegramService telegramService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/student/username/{telegramUsername}")
    public ResponseEntity<StudentTelegramDTO> getStudentTelegramByUsername(
            @PathVariable String telegramUsername
    ) {
        StudentTelegramDTO studentTelegramDTO = telegramService.getStudentTelegramByUsername(telegramUsername);

        return ResponseEntity.status(HttpStatus.OK).body(studentTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/recruiter/username/{telegramUsername}")
    public ResponseEntity<RecruiterTelegramDTO> getRecruiterTelegramByUsername(
            @PathVariable String telegramUsername
    ) {
        RecruiterTelegramDTO recruiterTelegramDTO = telegramService.getRecruiterTelegramByUsername(telegramUsername);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/student/{id}/setUserId")
    public ResponseEntity<StudentTelegramDTO> setStudentTelegramUserId(
            @PathVariable UUID id,
            @Valid @RequestBody SetTelegramUserIdReq setTelegramUserIdReq
    ) {
        StudentTelegramDTO studentTelegramDTO = telegramService.setStudentTelegramUserId(id, setTelegramUserIdReq);

        return ResponseEntity.status(HttpStatus.OK).body(studentTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/recruiter/{id}/setUserId")
    public ResponseEntity<RecruiterTelegramDTO> setRecruiterTelegramUserId(
            @PathVariable UUID id,
            @Valid @RequestBody SetTelegramUserIdReq setTelegramUserIdReq
    ) {
        RecruiterTelegramDTO recruiterTelegramDTO = telegramService.setRecruiterTelegramUserId(id, setTelegramUserIdReq);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/student/{id}/clearUserId")
    public ResponseEntity<StudentTelegramDTO> clearStudentTelegramUserId(
            @PathVariable UUID id
    ) {
        StudentTelegramDTO studentTelegramDTO = telegramService.clearStudentTelegramUserId(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentTelegramDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/recruiter/{id}/clearUserId")
    public ResponseEntity<RecruiterTelegramDTO> clearRecruiterTelegramUserId(
            @PathVariable UUID id
    ) {
        RecruiterTelegramDTO recruiterTelegramDTO = telegramService.clearRecruiterTelegramUserId(id);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterTelegramDTO);
    }
}

