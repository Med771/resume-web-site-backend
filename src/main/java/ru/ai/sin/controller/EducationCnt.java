package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.education.AddEducationReq;
import ru.ai.sin.dto.education.GetEducationRes;
import ru.ai.sin.dto.education.MergeEducationReq;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/education")
public class EducationCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetEducationRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<GetEducationRes>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetEducationRes> create(
            @RequestBody AddEducationReq educationReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "/merge")
    public ResponseEntity<GetEducationRes> merge(
            @RequestParam(defaultValue = "-1") long id,
            @RequestBody MergeEducationReq educationReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetEducationRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
