package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.GetSpecialityRes;
import ru.ai.sin.dto.speciality.MergeSpecialityReq;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/speciality")
public class SpecialityCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetSpecialityRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<GetSpecialityRes>> getAll(
            @RequestParam long page,
            @RequestParam long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetSpecialityRes> create(
            @RequestBody AddSpecialityReq specialityReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "/merge")
    public ResponseEntity<GetSpecialityRes> merge(
            @RequestParam long id,
            @RequestBody MergeSpecialityReq specialityReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetSpecialityRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
