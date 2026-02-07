package ru.ai.sin.logic.experience;

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

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.logic.experience.dto.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/experience")
public class ExperienceController {

    private final ExperienceService experienceService;

    private final SecurityHelper securityHelper;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<ExperienceDTO> getById(@PathVariable @Min(1) long id) {
        return ResponseEntity.ok(experienceService.getById(id));
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<ExperienceDTO>> findAllByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterExperienceReq filterExperienceReq) {
        if (filterExperienceReq.companyId() != null) {
            securityHelper.checkAdminRoleForFilter();
        }

        PageResponse<ExperienceDTO> experienceDTOs = experienceService.getAllByFilter(pageable, filterExperienceReq);

        return ResponseEntity.ok(experienceDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<ExperienceDTO> create(@Valid @RequestBody AddExperienceReq addExperienceReq) {
        ExperienceDTO experienceDTO = experienceService.create(addExperienceReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(experienceDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<ExperienceDTO> update(
            @PathVariable long id,

            @Valid @RequestBody UpdateExperienceReq updateExperienceReq
    ) {
        ExperienceDTO experienceDTO = experienceService.update(id, updateExperienceReq);

        return ResponseEntity.ok(experienceDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        experienceService.deleteById(id);
    }
}
