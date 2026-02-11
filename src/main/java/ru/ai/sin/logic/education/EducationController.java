package ru.ai.sin.logic.education;

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

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.education.dto.*;


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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<EducationDTO>> filter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterEducationReq filterEducationReq
    ) {
        PageResponse<EducationDTO> educationDTOs = educationService.getAllByFilter(pageable, filterEducationReq);

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
    public ResponseEntity<EducationDTO> updateById(
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
