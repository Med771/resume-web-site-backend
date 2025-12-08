package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SetSpecialityNameReq;
import ru.ai.sin.dto.speciality.SetSpecialitySkillsReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import ru.ai.sin.service.impl.SpecialityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/speciality")
public class SpecialityCnt {

    private final SpecialityService specialityService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<SpecialityDTO> getById(
            @PathVariable long id
    ) {
        SpecialityDTO specialityDTO = specialityService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<SpecialityDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageSpecialityNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageSpecialitySize
    ) {
        List<SpecialityDTO> specialityDTOs = specialityService.getAll(pageSpecialityNumber, pageSpecialitySize);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<SpecialityDTO> create(
            @Valid @RequestBody AddSpecialityReq specialityReq
    ) {
        SpecialityDTO specialityDTO = specialityService.create(specialityReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(specialityDTO);
    }

    @PutMapping(path = "/set/{id}/name")
    public ResponseEntity<SpecialityDTO> setNameById(
            @PathVariable long id,

            @Valid @RequestBody SetSpecialityNameReq setSpecialityNameReq
    ) {
        SpecialityDTO specialityDTO = specialityService.setNameById(id, setSpecialityNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }

    @PutMapping(path = "/set/{id}/skills")
    public ResponseEntity<SpecialityDTO> setSkillsById(
            @PathVariable long id,

            @Valid @RequestBody SetSpecialitySkillsReq setSpecialitySkillsReq
    ) {
        SpecialityDTO specialityDTO = specialityService.setSkillsById(id, setSpecialitySkillsReq);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<SpecialityDTO> deleteById(
            @PathVariable long id
    ) {
        SpecialityDTO specialityDTO = specialityService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }
}
