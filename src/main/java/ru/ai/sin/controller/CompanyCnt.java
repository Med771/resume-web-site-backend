package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyNameDTO;
import ru.ai.sin.service.impl.CompanyService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/company")
public class CompanyCnt {

    private final CompanyService companyService;

    @GetMapping(path = "/getById")
    public ResponseEntity<CompanyDTO> getById(
            @RequestParam long id) {
        CompanyDTO companyDTO = companyService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<CompanyDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        List<CompanyDTO> companyDTOs = companyService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTOs);
    }

    @PostMapping(path = "/getAllByName")
    public ResponseEntity<List<CompanyDTO>> getAllByName(
            @RequestBody CompanyNameDTO companyNameDTO) {
        List<CompanyDTO> companyDTOs = companyService.getAllByName(companyNameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<CompanyDTO> create(
            @RequestBody AddCompanyReq companyReq) {
        CompanyDTO companyDTO = companyService.create(companyReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(companyDTO);
    }

    @PutMapping(path = "/setNameById")
    public ResponseEntity<CompanyDTO> setNameById(
            @RequestParam long id,
            @RequestBody CompanyNameDTO companyNameDTO) {
        CompanyDTO companyDTO = companyService.setNameById(id, companyNameDTO);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<CompanyDTO> deleteById(
            @RequestParam long id) {
        CompanyDTO companyDTO = companyService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(companyDTO);
    }
}
