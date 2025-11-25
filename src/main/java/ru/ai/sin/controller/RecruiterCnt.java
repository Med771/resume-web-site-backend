package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterRes;
import ru.ai.sin.dto.recruiter.MergeRecruiterReq;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/admin/recruiter")
public class RecruiterCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetRecruiterRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<GetRecruiterRes>> getAll(
            @RequestParam long page,
            @RequestParam long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetRecruiterRes> create(
            @RequestBody AddRecruiterReq recruiterReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "/merge")
    public ResponseEntity<GetRecruiterRes> merge(
            @RequestParam UUID id,
            @RequestBody MergeRecruiterReq recruiterReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetRecruiterRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
