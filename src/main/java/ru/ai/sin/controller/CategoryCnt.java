package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.GetCategoryRes;
import ru.ai.sin.dto.category.MergeCategoryReq;
import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.GetCompanyRes;
import ru.ai.sin.dto.company.MergeCompanyReq;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/category")
public class CategoryCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetCategoryRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<GetCategoryRes>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetCategoryRes> create(
            @RequestBody AddCategoryReq categoryReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "/merge")
    public ResponseEntity<GetCategoryRes> merge(
            @RequestParam(defaultValue = "-1") long id,
            @RequestBody MergeCategoryReq categoryReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetCategoryRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
