package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.service.impl.SkillService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/skill")
public class SkillCnt {

    private final SkillService skillService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<SkillDTO> getById(
            @PathVariable long id
    ) {
        SkillDTO skillDTO = skillService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<SkillDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageSkillsNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageSkillsSize
    ) {
        List<SkillDTO> skillDTOs = skillService.getAll(pageSkillsNumber, pageSkillsSize);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<SkillDTO> create(
            @Valid @RequestBody AddSkillReq skillReq
    ) {
        SkillDTO skillDTO = skillService.create(skillReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(skillDTO);
    }

    @PutMapping(path = "/set/{id}/name")
    public ResponseEntity<SkillDTO> setNameById(
            @PathVariable long id,

            @Valid @RequestBody AddSkillReq addSkillReq
    ) {
        SkillDTO skillDTO = skillService.setNameById(id, addSkillReq);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<SkillDTO> deleteById(
            @PathVariable long id
    ) {
        SkillDTO skillDTO = skillService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }
}
