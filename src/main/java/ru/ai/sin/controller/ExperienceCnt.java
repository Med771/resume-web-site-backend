package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.experience.*;
import ru.ai.sin.service.impl.ExperienceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/experience")
public class ExperienceCnt {

    private final ExperienceService experienceService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<ExperienceDTO> getById(
            @PathVariable long id
    ) {
        ExperienceDTO experienceDTO = experienceService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

    /* TODO: Implement statistics by Q1 2026

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/aboutGetByCompanyId/{id}")
    public ResponseEntity<GetAboutCompanyRes> aboutGetByCompanyId(
            @PathVariable long id,

            @Min(0) @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageExperienceSize
    ) {
        GetAboutCompanyRes getAboutCompanyResDTO = experienceService
                .getAboutCompanyById(
                        id,
                        pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutCompanyResDTO);
    }
    */

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/aboutGetByStudentId/{id}")
    public ResponseEntity<GetAboutStudentRes> aboutGetByStudentId(
            @PathVariable UUID id,

            @Min(0) @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageExperienceSize
    ) {
        GetAboutStudentRes getAboutStudentResDTO = experienceService
                .getAboutStudentById(
                        id,
                        pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutStudentResDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<ExperienceDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageExperienceSize
    ) {
        List<ExperienceDTO> experienceDTOs = experienceService.getAll(
                pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/create")
    public ResponseEntity<ExperienceDTO> create(
            @Valid @RequestBody AddExperienceReq experienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.create(experienceReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(experienceDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<ExperienceDTO> update(
            @PathVariable long id,

            @Valid @RequestBody AddExperienceReq experienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.update(id, experienceReq);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<ExperienceDTO> deleteById(
            @PathVariable long id
    ) {
        ExperienceDTO experienceDTO = experienceService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }
}
