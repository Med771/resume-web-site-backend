package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SetSkillNameReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.service.impl.SkillService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/skill")
public class SkillCnt {

    private final SkillService skillService;

    @GetMapping(path = "/getById")
    public ResponseEntity<SkillDTO> getById(
            @RequestParam long id) {
        SkillDTO skillDTO = skillService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<SkillDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        List<SkillDTO> skillDTOs = skillService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<SkillDTO> create(
            @RequestBody AddSkillReq skillReq) {
        SkillDTO skillDTO = skillService.create(skillReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(skillDTO);
    }

    @PostMapping(path = "/setNameById")
    public ResponseEntity<SkillDTO> setNameById(
            @RequestParam long id,
            @RequestBody SetSkillNameReq setSkillNameReq) {
        SkillDTO skillDTO = skillService.setNameById(id, setSkillNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<SkillDTO> deleteById(
            @RequestParam long id) {
        SkillDTO skillDTO = skillService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(skillDTO);
    }
}
