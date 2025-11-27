package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterNameReq;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;
import ru.ai.sin.service.impl.RecruiterService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/recruiter")
public class RecruiterCnt {

    private final RecruiterService recruiterService;

    @GetMapping(path = "/getById")
    public ResponseEntity<RecruiterDTO> getById(
            @RequestParam UUID id) {
        RecruiterDTO recruiterDTO = recruiterService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<RecruiterDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        List<RecruiterDTO> recruiterDTOs = recruiterService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTOs);
    }

    @PostMapping(path = "/getAllByName")
    public ResponseEntity<List<RecruiterDTO>> getAllByName(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestBody GetRecruiterNameReq getRecruiterNameReq) {
        List<RecruiterDTO> recruiterDTOs = recruiterService.getAllByName(page, size, getRecruiterNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<RecruiterDTO> create(
            @RequestBody AddRecruiterReq recruiterReq) {
        RecruiterDTO recruiterDTO = recruiterService.create(recruiterReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(recruiterDTO);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<RecruiterDTO> update(
            @RequestParam UUID id,
            @RequestBody UpdateRecruiterReq recruiterReq) {
        RecruiterDTO recruiterDTO = recruiterService.update(id, recruiterReq);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<RecruiterDTO> deleteById(
            @RequestParam UUID id) {
        RecruiterDTO recruiterDTO = recruiterService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTO);
    }
}
