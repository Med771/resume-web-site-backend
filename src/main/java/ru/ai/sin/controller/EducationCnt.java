package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.education.*;
import ru.ai.sin.service.impl.EducationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/education")
public class EducationCnt {

    private final EducationService educationService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<EducationDTO> getById(
            @PathVariable long id
    ) {
        EducationDTO educationDTO = educationService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<EducationDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageEducationNumber,
            @Min(1) @RequestParam(defaultValue = "10") int sizeEducationSize
    ) {
        List<EducationDTO> educationDTOs = educationService
                .getAll(pageEducationNumber, sizeEducationSize);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<EducationDTO> create(
            @Valid @RequestBody AddEducationReq addEducationReq
    ) {
        EducationDTO educationDTO = educationService
                .create(addEducationReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(educationDTO);
    }

    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<EducationDTO> update(
            @PathVariable long id,

            @Valid @RequestBody AddEducationReq addEducationReq
    ) {
        EducationDTO educationDTO = educationService
                .update(
                        id,
                        addEducationReq);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<EducationDTO> deleteById(
            @PathVariable long id
    ) {
        EducationDTO educationDTO = educationService.deleteById(
                id);

        return ResponseEntity.status(HttpStatus.OK).body(educationDTO);
    }
}
