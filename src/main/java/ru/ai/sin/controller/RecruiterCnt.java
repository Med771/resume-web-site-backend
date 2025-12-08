package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
@RequestMapping(path = "/recruiter")
public class RecruiterCnt {

    private final RecruiterService recruiterService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<RecruiterDTO> getById(
            @PathVariable UUID id
    ) {
        RecruiterDTO recruiterDTO = recruiterService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<RecruiterDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageRecruiterNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageRecruiterSize
    ) {
        List<RecruiterDTO> recruiterDTOs = recruiterService.getAll(
                pageRecruiterNumber, pageRecruiterSize);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTOs);
    }

    @PostMapping(path = "/getAllByCompanyName")
    public ResponseEntity<List<RecruiterDTO>> getAllByCompanyName(
            @Min(0) @RequestParam(defaultValue = "0") int pageRecruiterNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageRecruiterSize,

            @Valid @RequestBody GetRecruiterNameReq getRecruiterNameReq
    ) {
        List<RecruiterDTO> recruiterDTOs = recruiterService
                .getAllByCompanyName(
                        pageRecruiterNumber, pageRecruiterSize,
                        getRecruiterNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<RecruiterDTO> create(
            @Valid @RequestBody AddRecruiterReq recruiterReq
    ) {
        RecruiterDTO recruiterDTO = recruiterService.create(recruiterReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(recruiterDTO);
    }

    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<RecruiterDTO> updateById(
            @PathVariable UUID id,

            @Valid @RequestBody UpdateRecruiterReq recruiterReq
    ) {
        RecruiterDTO recruiterDTO = recruiterService
                .update(
                        id,
                        recruiterReq);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<RecruiterDTO> deleteById(
            @PathVariable UUID id
    ) {
        RecruiterDTO recruiterDTO = recruiterService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(recruiterDTO);
    }
}
