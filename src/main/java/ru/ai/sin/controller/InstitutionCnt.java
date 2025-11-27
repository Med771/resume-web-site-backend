package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.institution.AddInstitutionReq;
import ru.ai.sin.dto.institution.GetAboutEducationRes;
import ru.ai.sin.dto.institution.GetAboutStudentRes;
import ru.ai.sin.dto.institution.InstitutionDTO;
import ru.ai.sin.service.impl.InstitutionService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/institution")
public class InstitutionCnt {

    private final InstitutionService institutionService;

    @GetMapping(path = "/getById")
    public ResponseEntity<InstitutionDTO> getById(
            @RequestParam long id) {
        InstitutionDTO institutionDTO = institutionService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }

    @GetMapping(path = "/getByEducationId")
    public ResponseEntity<GetAboutEducationRes> getByEducationId(
            @RequestParam long id) {
        GetAboutEducationRes getAboutEducationRes = institutionService.getByEducationId(id);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutEducationRes);
    }

    @GetMapping(path = "/getByStudentId")
    public ResponseEntity<GetAboutStudentRes> getByStudentId(
            @RequestParam UUID id) {
        GetAboutStudentRes getAboutStudentRes = institutionService.getByStudentId(id);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutStudentRes);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<InstitutionDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        List<InstitutionDTO> institutionDTOs = institutionService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<InstitutionDTO> create(
            @RequestBody AddInstitutionReq institutionReq) {
        InstitutionDTO institutionDTO = institutionService.create(institutionReq);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<InstitutionDTO> update(
            @RequestParam long id,
            @RequestBody AddInstitutionReq institutionReq) {
        InstitutionDTO institutionDTO = institutionService.update(id, institutionReq);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<InstitutionDTO> deleteById(
            @RequestParam long id) {
        InstitutionDTO institutionDTO = institutionService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }
}
