package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.request.AddRequestReq;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.request.RequestUpdateStatusReq;
import ru.ai.sin.dto.request.UpdateRequestByChatReq;
import ru.ai.sin.service.impl.RequestService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/request")
public class RequestController {

    private final RequestService requestService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable @Min(1) long id) {
        return ResponseEntity.ok(requestService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<RequestDTO>> getByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
            @Valid @RequestBody RequestFilterReq requestFilterReq) {
        return ResponseEntity.ok(requestService.getByFilter(pageable, requestFilterReq));
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<RequestDTO> create(@Valid @RequestBody AddRequestReq addRequestReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(requestService.create(addRequestReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/status/{id}")
    public ResponseEntity<RequestDTO> updateStatus(
            @PathVariable @Min(1) long id,
            @Valid @RequestBody RequestUpdateStatusReq requestUpdateStatusReq) {
        return ResponseEntity.ok(requestService.updateStatus(id, requestUpdateStatusReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(path = "/by-chat/{chatId}")
    public ResponseEntity<RequestDTO> updateByChatId(
            @PathVariable String chatId,
            @Valid @RequestBody UpdateRequestByChatReq updateRequestByChatReq) {
        return ResponseEntity.ok(requestService.updateByChatId(chatId, updateRequestByChatReq));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        requestService.deleteById(id);
    }
}
