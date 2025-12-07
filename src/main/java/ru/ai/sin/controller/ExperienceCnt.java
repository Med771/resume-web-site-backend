package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.experience.*;
import ru.ai.sin.service.impl.ExperienceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/experience")
public class ExperienceCnt {

    private final ExperienceService experienceService;

    @GetMapping(path = "/getById")
    public ResponseEntity<ExperienceDTO> getById(
            @RequestParam long id
    ) {
        ExperienceDTO experienceDTO = experienceService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

    @GetMapping(path = "/aboutGetByCompanyId"
    )
    public ResponseEntity<GetAboutCompanyRes> aboutGetByCompanyId(
            @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @RequestParam(defaultValue = "10") int pageExperienceSize,
            @RequestParam long id
    ) {
        GetAboutCompanyRes getAboutCompanyResDTO = experienceService.getAboutCompanyById(
                id,
                pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutCompanyResDTO);
    }

    @GetMapping(path = "/aboutGetByStudentId")
    public ResponseEntity<GetAboutStudentRes> aboutGetByStudentId(
            @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @RequestParam(defaultValue = "10") int pageExperienceSize,
            @RequestParam UUID id
    ) {
        GetAboutStudentRes getAboutStudentResDTO = experienceService.getAboutStudentById(
                id,
                pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutStudentResDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<ExperienceDTO>> getAll(
            @RequestParam(defaultValue = "0") int pageExperienceNumber,
            @RequestParam(defaultValue = "10") int pageExperienceSize
    ) {
        List<ExperienceDTO> experienceDTOs = experienceService.getAll(
                pageExperienceNumber, pageExperienceSize);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ExperienceDTO> create(
            @RequestBody AddExperienceReq experienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.create(experienceReq);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<ExperienceDTO> update(
            @RequestParam(defaultValue = "-1") long id,
            @RequestBody AddExperienceReq experienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.update(id, experienceReq);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<ExperienceDTO> deleteById(
            @RequestParam long id
    ) {
        ExperienceDTO experienceDTO = experienceService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(experienceDTO);
    }
}
