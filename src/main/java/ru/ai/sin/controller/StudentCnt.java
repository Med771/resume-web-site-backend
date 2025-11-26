package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.student.StudentCardDTO;
import ru.ai.sin.dto.student.StudentDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/student")
public class StudentCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<StudentDTO> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getCards")
    public ResponseEntity<List<StudentCardDTO>> getCards(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<StudentDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<StudentDTO> create() {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PostMapping(path = "/")

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<StudentDTO> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
