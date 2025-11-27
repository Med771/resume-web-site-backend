package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SetSpecialityNameReq;
import ru.ai.sin.dto.speciality.SetSpecialitySkillsReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import ru.ai.sin.service.impl.SpecialityService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/speciality")
public class SpecialityCnt {

    private final SpecialityService specialityService;

    @GetMapping(path = "/getById")
    public ResponseEntity<SpecialityDTO> getById(
            @RequestParam long id) {
        SpecialityDTO specialityDTO = specialityService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<SpecialityDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        List<SpecialityDTO> specialityDTOs = specialityService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<SpecialityDTO> create(
            @RequestBody AddSpecialityReq specialityReq) {
        SpecialityDTO specialityDTO = specialityService.create(specialityReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(specialityDTO);
    }

    @PutMapping(path = "/setNameById")
    public ResponseEntity<SpecialityDTO> setNameById(
            @RequestParam long id,
            @RequestBody SetSpecialityNameReq setSpecialityNameReq) {
        SpecialityDTO specialityDTO = specialityService.setNameById(id, setSpecialityNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }

    @PutMapping(path = "/setSkillsById")
    public ResponseEntity<SpecialityDTO> setSkillsById(
            @RequestParam long id,
            @RequestBody SetSpecialitySkillsReq setSpecialitySkillsReq) {
        SpecialityDTO specialityDTO = specialityService.setSkillsById(id, setSpecialitySkillsReq);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<SpecialityDTO> deleteById(
            @RequestParam long id) {
        SpecialityDTO specialityDTO = specialityService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(specialityDTO);
    }
}
