package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.GetCompanyRes;
import ru.ai.sin.dto.company.MergeCompanyReq;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/company")
public class CompanyCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetCompanyRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<GetCompanyRes>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetCompanyRes> create(
            @RequestBody AddCompanyReq companyReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "/merge")
    public ResponseEntity<GetCompanyRes> merge(
            @RequestParam(defaultValue = "-1") long id,
            @RequestBody MergeCompanyReq companyReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetCompanyRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
