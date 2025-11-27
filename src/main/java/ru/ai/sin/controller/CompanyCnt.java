package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyNameDTO;

import java.util.List;

@RestController
@RequestMapping(path = "/company")
public class CompanyCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<CompanyDTO> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getByName")
    public ResponseEntity<CompanyDTO> getByName(
            @RequestBody CompanyNameDTO companyNameDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<CompanyDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<CompanyDTO> create(
            @RequestBody AddCompanyReq companyReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping(path = "/setName")
    public ResponseEntity<CompanyDTO> setName(
            @RequestParam long id,
            @RequestBody CompanyNameDTO companyNameDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<CompanyDTO> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
