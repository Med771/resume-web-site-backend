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
            @RequestParam long id) {
        EducationDTO educationDTO = educationService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<EducationDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        List<EducationDTO> educationDTOs = educationService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<EducationDTO> create(
            @RequestBody AddEducationReq educationReq) {
        EducationDTO educationDTO = educationService.create(educationReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationDTO);
    }

    @PutMapping(path = "/setInstitutionById")
    public ResponseEntity<EducationDTO> setInstitutionById(
            @RequestParam long id,
            @RequestBody SetInstitutionReq setInstitutionReq) {
        EducationDTO educationDTO = educationService.setInstitutionById(id, setInstitutionReq);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @PutMapping(path = "/setAdditionalInfoById")
    public ResponseEntity<EducationDTO> setAdditionalInfoById(
            @RequestParam long id,
            @RequestBody SetEducationInfoReq setEducationInfoReq) {
        EducationDTO educationDTO = educationService.setAdditionalInfoById(id, setEducationInfoReq);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @PutMapping(path = "/setSkillsById")
    public ResponseEntity<EducationDTO> setSkillsById(
            @RequestParam long id,
            @RequestBody SetEducationSkillsReq setEducationSkillsReq) {
        EducationDTO educationDTO = educationService.setSkillsById(id, setEducationSkillsReq);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<EducationDTO> deleteById(
            @RequestParam long id) {
        EducationDTO educationDTO = educationService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }
}
