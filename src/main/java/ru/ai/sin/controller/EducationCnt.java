package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.education.*;
import ru.ai.sin.service.impl.EducationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/education")
public class EducationCnt {

    private final EducationService educationService;

    @GetMapping(path = "/getById")
    public ResponseEntity<EducationDTO> getById(
            @RequestParam long id,
            @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @RequestParam(defaultValue = "10") int pageInstitutionSize
    ) {
        EducationDTO educationDTO = educationService.getById(
                id,
                pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<EducationDTO>> getAll(
            @RequestParam(defaultValue = "0") int pageEducationNumber,
            @RequestParam(defaultValue = "10") int sizeEducationSize,
            @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @RequestParam(defaultValue = "10") int pageInstitutionSize
    ) {
        List<EducationDTO> educationDTOs = educationService.getAll(
                pageEducationNumber, sizeEducationSize,
                pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<EducationDTO> create(
            @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @RequestParam(defaultValue = "10") int pageInstitutionSize,
            @RequestBody AddEducationReq addEducationReq
    ) {
        EducationDTO educationDTO = educationService.create(
                addEducationReq, pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationDTO);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<EducationDTO> update(
            @RequestParam long id,
            @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @RequestParam(defaultValue = "10") int pageInstitutionSize,
            @RequestBody AddEducationReq addEducationReq
    ) {
        EducationDTO educationDTO = educationService.update(
                id, addEducationReq, pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<EducationDTO> deleteById(
            @RequestParam long id,
            @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @RequestParam(defaultValue = "10") int pageInstitutionSize
    ) {
        EducationDTO educationDTO = educationService.deleteById(
                id,
                pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }
}
