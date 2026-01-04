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
import ru.ai.sin.dto.education.*;

import ru.ai.sin.service.impl.EducationService;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/education")
public class EducationController {

    private final EducationService educationService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<EducationDTO> getById(@PathVariable @Min(1) long id) {
        EducationDTO educationDTO = educationService.getById(id);

        return ResponseEntity.ok(educationDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/all")
    public ResponseEntity<PageResponse<EducationDTO>> getAll(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        PageResponse<EducationDTO> educationDTOs = educationService.getAll(pageable);

        return ResponseEntity.ok(educationDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<EducationDTO> create(@Valid @RequestBody AddEducationReq addEducationReq) {
        EducationDTO educationDTO = educationService.create(addEducationReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<EducationDTO> update(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody UpdateEducationReq updateEducationReq
    ) {
        EducationDTO educationDTO = educationService.update(id, updateEducationReq);

        return ResponseEntity.ok(educationDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        educationService.deleteById(id);
    }
}
