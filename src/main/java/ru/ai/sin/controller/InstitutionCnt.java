package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping(path = "/institution")
public class InstitutionCnt {

    private final InstitutionService institutionService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<InstitutionDTO> getById(
            @PathVariable long id
    ) {
        InstitutionDTO institutionDTO = institutionService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }

    @GetMapping(path = "/getByEducationId/{id}")
    public ResponseEntity<GetAboutEducationRes> getByEducationId(
            @PathVariable long id,

            @Min(0) @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageInstitutionSize
    ) {
        GetAboutEducationRes getAboutEducationRes = institutionService
                .getByEducationId(
                        id,
                        pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutEducationRes);
    }

    @GetMapping(path = "/getByStudentId/{id}")
    public ResponseEntity<GetAboutStudentRes> getByStudentId(
            @PathVariable UUID id,

            @Min(0) @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageInstitutionSize
    ) {
        GetAboutStudentRes getAboutStudentRes = institutionService
                .getByStudentId(
                        id,
                        pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(getAboutStudentRes);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<InstitutionDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageInstitutionNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageInstitutionSize
    ) {
        List<InstitutionDTO> institutionDTOs = institutionService.getAll(
                pageInstitutionNumber, pageInstitutionSize);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<InstitutionDTO> create(
            @Valid @RequestBody AddInstitutionReq institutionReq
    ) {
        InstitutionDTO institutionDTO = institutionService.create(institutionReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(institutionDTO);
    }

    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<InstitutionDTO> update(
            @PathVariable long id,

            @Valid @RequestBody AddInstitutionReq institutionReq
    ) {
        InstitutionDTO institutionDTO = institutionService.update(id, institutionReq);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<InstitutionDTO> deleteById(
            @PathVariable long id
    ) {
        InstitutionDTO institutionDTO = institutionService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(institutionDTO);
    }
}
