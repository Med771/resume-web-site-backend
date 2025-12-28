package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

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
import ru.ai.sin.dto.experience.*;

import ru.ai.sin.service.impl.ExperienceService;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/experience")
public class ExperienceCnt {

    private final ExperienceService experienceService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "{id}")
    public ResponseEntity<ExperienceDTO> getById(@PathVariable @Min(1) long id) {
        return ResponseEntity.ok(experienceService.getById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "filter")
    public ResponseEntity<PageResponse<ExperienceDTO>> findAllByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody ExperienceFilterReq experienceFilterReq) {
        PageResponse<ExperienceDTO> experienceDTOs = experienceService.getAllByFilter(pageable, experienceFilterReq);

        return ResponseEntity.ok(experienceDTOs);
    }

    @Deprecated
    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/aboutGetByStudentId/{id}")
    public ResponseEntity<GetAboutStudentRes> aboutGetByStudentId(
            @PathVariable UUID id,

            @Min(0) @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageExperienceSize) {
        GetAboutStudentRes getAboutStudentResDTO = experienceService
                .getAboutStudentById(
                        id,
                        pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutStudentResDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ExperienceDTO> create(@Valid @RequestBody AddExperienceReq addExperienceReq) {
        ExperienceDTO experienceDTO = experienceService.create(addExperienceReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(experienceDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<ExperienceDTO> update(
            @PathVariable long id,

            @Valid @RequestBody UpdateExperienceReq updateExperienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.update(id, updateExperienceReq);

        return ResponseEntity.ok(experienceDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        experienceService.deleteById(id);
    }
}
