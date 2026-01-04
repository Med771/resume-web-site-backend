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
import ru.ai.sin.dto.request.RequestNewChatReq;
import ru.ai.sin.dto.request.RequestUpdateStatusReq;
import ru.ai.sin.service.impl.RequestService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/request")
public class RequestController {

    private final RequestService requestService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<RequestDTO> getById(@PathVariable("id") @Min(1) long id) {
        RequestDTO requestDTO = requestService.getById(id);

        return ResponseEntity.ok(requestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<RequestDTO>> getByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody RequestFilterReq requestFilterReq
            ) {
        PageResponse<RequestDTO> requestDTOs = requestService.getByFilter(pageable, requestFilterReq);

        return ResponseEntity.ok(requestDTOs);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping()
    public ResponseEntity<RequestDTO> create(
            @Valid @RequestBody AddRequestReq addRequestReq
    ) {
        RequestDTO requestDTO = requestService.create(addRequestReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(requestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/newChat/{id}")
    public ResponseEntity<RequestDTO> newChatById(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody RequestNewChatReq requestNewChatReq
    ) {
        RequestDTO requestDTO = requestService.newChatById(id, requestNewChatReq);

        return ResponseEntity.ok(requestDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/status/{id}")
    public ResponseEntity<RequestDTO> updateStatus(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody RequestUpdateStatusReq requestUpdateStatusReq
    ) {
        RequestDTO requestDTO = requestService.updateStatus(id, requestUpdateStatusReq);

        return ResponseEntity.ok(requestDTO);
    }
}
