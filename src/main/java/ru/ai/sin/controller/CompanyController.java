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

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyFilterReq;
import ru.ai.sin.dto.company.UpdateCompanyReq;

import ru.ai.sin.service.impl.CompanyService;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/company")
public class CompanyController {

    private final CompanyService companyService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<CompanyDTO> getById(@PathVariable @Min(1) long id) {
        CompanyDTO companyDTO = companyService.getById(id);

        return ResponseEntity.ok(companyDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<CompanyDTO>> getAllByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody CompanyFilterReq companyFilterReq
    ) {
        PageResponse<CompanyDTO> companyDTOs = companyService.getAllByFilter(
                pageable, companyFilterReq);

        return ResponseEntity.ok(companyDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<CompanyDTO> create(@Valid @RequestBody AddCompanyReq companyReq) {
        CompanyDTO companyDTO = companyService.create(companyReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<CompanyDTO> updateById(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody UpdateCompanyReq updateCompanyReq
    ) {
        CompanyDTO companyDTO = companyService.updateById(id, updateCompanyReq);

        return ResponseEntity.ok(companyDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        companyService.deleteById(id);
    }
}
