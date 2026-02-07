package ru.ai.sin.logic.speciality;

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

import ru.ai.sin.logic.speciality.dto.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/speciality")
public class SpecialityController {

    private final SpecialityService specialityService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<SpecialityDTO> getById(@PathVariable @Min(1) long id) {
        SpecialityDTO specialityDTO = specialityService.getById(id);

        return ResponseEntity.ok(specialityDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<SpecialityDTO>> filter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody FilterSpecialityReq filterSpecialityReq
    ) {
        PageResponse<SpecialityDTO> specialityDTOs = specialityService.getAllByFilter(pageable, filterSpecialityReq);

        return ResponseEntity.ok(specialityDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<SpecialityDTO> createById(@Valid @RequestBody AddSpecialityReq specialityReq) {
        SpecialityDTO specialityDTO = specialityService.create(specialityReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(specialityDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<SpecialityDTO> updateById(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody UpdateSpecialityReq updateSpecialityReq
    ) {
        SpecialityDTO specialityDTO = specialityService.update(id, updateSpecialityReq);

        return ResponseEntity.ok(specialityDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        specialityService.deleteById(id);
    }
}
