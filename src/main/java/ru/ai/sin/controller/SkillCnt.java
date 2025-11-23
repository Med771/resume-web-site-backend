package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.GetSkillRes;
import ru.ai.sin.dto.skill.MergeSkillReq;

@RestController
@RequestMapping(path = "/admin/skill")
public class SkillCnt {

    @GetMapping(path = "getById")
    public ResponseEntity<GetSkillRes> getById(@RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "create")
    public ResponseEntity<GetSkillRes> create(@RequestBody AddSkillReq skillReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "merge")
    public ResponseEntity<GetSkillRes> merge(@RequestBody MergeSkillReq skillReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "deleteById")
    public ResponseEntity<GetSkillRes> deleteById(@RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
