package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.GetCompanyNameReq;
import ru.ai.sin.service.impl.CompanyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/company")
public class CompanyCnt {

    private final CompanyService companyService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<CompanyDTO> getById(
            @PathVariable long id
    ) {
        CompanyDTO companyDTO = companyService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<CompanyDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageCompanyNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageCompanySize
    ) {
        List<CompanyDTO> companyDTOs = companyService.getAll(
                pageCompanyNumber, pageCompanySize);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTOs);
    }

    @PostMapping(path = "/getAllByName")
    public ResponseEntity<List<CompanyDTO>> getAllByName(
            @Min(0) @RequestParam(defaultValue = "0") int pageCompanyNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageCompanySize,

            @Valid @RequestBody GetCompanyNameReq getCompanyNameReq
    ) {
        List<CompanyDTO> companyDTOs = companyService.getAllByName(
                pageCompanyNumber, pageCompanySize, getCompanyNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<CompanyDTO> create(
            @Valid @RequestBody AddCompanyReq companyReq
    ) {
        CompanyDTO companyDTO = companyService.create(companyReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyDTO);
    }

    @PutMapping(path = "/set/{id}/name")
    public ResponseEntity<CompanyDTO> setNameById(
            @PathVariable long id,

            @Valid @RequestBody GetCompanyNameReq getCompanyNameReq
    ) {
        CompanyDTO companyDTO = companyService.setNameById(id, getCompanyNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<CompanyDTO> deleteById(
            @PathVariable long id
    ) {
        CompanyDTO companyDTO = companyService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }
}
