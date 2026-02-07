package ru.ai.sin.logic.skill;

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

import ru.ai.sin.logic.skill.dto.*;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/skill")
public class SkillController {

    private final SkillService skillService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<SkillDTO> getById(@PathVariable @Min(1) long id) {
        SkillDTO skillDTO = skillService.getById(id);

        return ResponseEntity.ok(skillDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<SkillDTO>> filter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterSkillReq filterSkillReq
    ) {
        PageResponse<SkillDTO> skillDTOs = skillService.getAllByFilter(pageable, filterSkillReq);

        return ResponseEntity.ok(skillDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<SkillDTO> createById(@Valid @RequestBody AddSkillReq skillReq) {
        SkillDTO skillDTO = skillService.create(skillReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(skillDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<SkillDTO> updateById(
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
