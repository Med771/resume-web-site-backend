package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<ExperienceDTO> getById(
            @PathVariable long id
    ) {
        ExperienceDTO experienceDTO = experienceService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

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

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<ExperienceDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageExperienceSize
    ) {
        List<ExperienceDTO> experienceDTOs = experienceService.getAll(
                pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ExperienceDTO> create(
            @Valid @RequestBody AddExperienceReq experienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.create(experienceReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(experienceDTO);
    }

    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<ExperienceDTO> update(
            @PathVariable long id,

            @Valid @RequestBody AddExperienceReq experienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.update(id, experienceReq);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<ExperienceDTO> deleteById(
            @PathVariable long id
    ) {
        ExperienceDTO experienceDTO = experienceService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }
}
