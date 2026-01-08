package ru.ai.sin.controller;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.recruiter.RecruiterFilterReq;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;

import ru.ai.sin.service.impl.RecruiterService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/recruiter")
public class RecruiterController {

    private final RecruiterService recruiterService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<RecruiterDTO> getById(
            @PathVariable @NotNull UUID id
    ) {
        RecruiterDTO recruiterDTO = recruiterService.getById(id);

        return ResponseEntity.ok(recruiterDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<RecruiterDTO>> filter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody RecruiterFilterReq recruiterFilterReq) {
        PageResponse<RecruiterDTO> recruiterDTOs = recruiterService.getAllByFilter(pageable, recruiterFilterReq);

        return ResponseEntity.ok(recruiterDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<RecruiterDTO> updateById(
            @PathVariable @NotNull UUID id,

            @Valid @RequestBody UpdateRecruiterReq recruiterReq
    ) {
        RecruiterDTO recruiterDTO = recruiterService.update(id, recruiterReq);

        return ResponseEntity.ok(recruiterDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @NotNull UUID id) {
        recruiterService.deleteById(id);
    }
}
