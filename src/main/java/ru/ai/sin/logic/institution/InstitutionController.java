package ru.ai.sin.logic.institution;

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

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.institution.dto.*;

import ru.ai.sin.helper.SecurityHelper;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    private final SecurityHelper securityHelper;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<InstitutionDTO> getById(@PathVariable @Min(1) long id) {
        InstitutionDTO institutionDTO = institutionService.getById(id);

        return ResponseEntity.ok(institutionDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<InstitutionDTO>> findAllByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterInstitutionReq filterInstitutionReq
    ) {
        if (filterInstitutionReq.educationId() != null) {
            securityHelper.checkAdminRoleForFilter();
        }

        PageResponse<InstitutionDTO> experienceDTOs = institutionService.getAllByFilter(pageable, filterInstitutionReq);

        return ResponseEntity.ok(experienceDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<InstitutionDTO> create(@Valid @RequestBody AddInstitutionReq institutionReq) {
        InstitutionDTO institutionDTO = institutionService.create(institutionReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(institutionDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<InstitutionDTO> update(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody UpdateInstitutionReq updateInstitutionReq
    ) {
        InstitutionDTO institutionDTO = institutionService.update(id, updateInstitutionReq);

        return ResponseEntity.ok(institutionDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        institutionService.deleteById(id);
    }
}
