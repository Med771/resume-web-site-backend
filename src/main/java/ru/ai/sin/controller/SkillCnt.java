package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;

import ru.ai.sin.dto.skill.SkillFilterReq;
import ru.ai.sin.dto.skill.UpdateSkillReq;

import ru.ai.sin.service.impl.SkillService;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/skill")
public class SkillCnt {

    private final SkillService skillService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<SkillDTO> getById(@PathVariable @Min(1) long id) {
        SkillDTO skillDTO = skillService.getById(id);

        return ResponseEntity.ok(skillDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<SkillDTO>> filter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody SkillFilterReq skillFilterReq
    ) {
        PageResponse<SkillDTO> skillDTOs = skillService.getAllByFilter(pageable, skillFilterReq);

        return ResponseEntity.ok(skillDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<SkillDTO> create(@Valid @RequestBody AddSkillReq skillReq) {
        SkillDTO skillDTO = skillService.create(skillReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(skillDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<SkillDTO> setNameById(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody UpdateSkillReq updateSkillReq
            ) {
        SkillDTO skillDTO = skillService.updateById(id, updateSkillReq);

        return ResponseEntity.ok(skillDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable long id) {
        skillService.deleteById(id);
    }
}
