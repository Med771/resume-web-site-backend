package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.dto.student.*;

import ru.ai.sin.service.impl.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/student")
public class StudentCnt {

    private final StudentService studentService;

    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<StudentDTO> getById(
            @PathVariable UUID id
    ) {
        StudentDTO studentDTO = studentService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }

    @GetMapping(path = "/getAllCards")
    public ResponseEntity<List<StudentCardDTO>> getAllCards(
            @Min(0) @RequestParam(defaultValue = "0") int pageStudentNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageStudentSize
    ) {
        List<StudentCardDTO> studentCardDTOs = studentService
                .getAllCards(
                        pageStudentNumber, pageStudentSize);

        return ResponseEntity.status(HttpStatus.OK).body(studentCardDTOs);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<StudentDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pageStudentNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageStudentSize
    ) {
        List<StudentDTO> studentDTOs = studentService
                .getAll(
                        pageStudentNumber, pageStudentSize);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTOs);
    }

    @PostMapping(path = "/getAllByFilters")
    public ResponseEntity<List<StudentCardDTO>> getAllByFilters(
            @Min(0) @RequestParam(defaultValue = "0") int pageStudentNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pageStudentSize,

            @Valid @RequestBody GetStudentFilterReq getStudentFilterReq
    ) {
        List<StudentCardDTO> studentCardDTOs = studentService
                .getAllByFilters(
                        pageStudentNumber, pageStudentSize,
                        getStudentFilterReq);

        return ResponseEntity.status(HttpStatus.OK).body(studentCardDTOs);
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentDTO> create(
            @RequestPart("avatarFile") MultipartFile multipartFile,
            @Valid @RequestPart(value = "profileData") AddStudentReq addStudentReq
    ) {
        StudentDTO studentDTO = studentService.create(multipartFile, addStudentReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @PutMapping(path = "updateById/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentDTO> updateById(
            @PathVariable("id") UUID id,

            @RequestPart("avatarFile") MultipartFile multipartFile,
            @Valid @RequestPart(value = "profileData") UpdateStudentReq updateStudentReq
    ) {
        StudentDTO studentDTO = studentService.update(id, multipartFile, updateStudentReq);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }

    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<StudentDTO> deleteById(
            @PathVariable UUID id
    ) {
        StudentDTO studentDTO = studentService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }
}
