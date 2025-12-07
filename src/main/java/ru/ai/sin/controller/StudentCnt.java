package ru.ai.sin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.dto.student.*;

import ru.ai.sin.service.impl.StudentService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/student")
public class StudentCnt {

    private final StudentService studentService;

    private final ObjectMapper objectMapper;

    @GetMapping(path = "/getById")
    public ResponseEntity<StudentDTO> getById(
            @RequestParam UUID id) {
        StudentDTO studentDTO = studentService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }

    @GetMapping(path = "/getAllCards")
    public ResponseEntity<List<StudentCardDTO>> getAllCards(
            @RequestParam(defaultValue = "0") int pageStudentNumber,
            @RequestParam(defaultValue = "10") int pageStudentSize
    ) {
        List<StudentCardDTO> studentCardDTOs = studentService.getAllCards(pageStudentNumber, pageStudentSize);

        return ResponseEntity.status(HttpStatus.OK).body(studentCardDTOs);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<StudentDTO>> getAll(
            @RequestParam(defaultValue = "0") int pageStudentNumber,
            @RequestParam(defaultValue = "10") int pageStudentSize
    ) {
        List<StudentDTO> studentDTOs = studentService.getAll(pageStudentNumber, pageStudentSize);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTOs);
    }

    @PostMapping(path = "/getAllByFilters")
    public ResponseEntity<List<StudentCardDTO>> getAllByFilters(
            @RequestParam(defaultValue = "0") int pageStudentNumber,
            @RequestParam(defaultValue = "10") int pageStudentSize,
            @RequestBody GetStudentFilterReq getStudentFilterReq
    ) {
        List<StudentCardDTO> studentCardDTOs = studentService.getAllByFilters(pageStudentNumber, pageStudentSize, getStudentFilterReq);

        return ResponseEntity.status(HttpStatus.OK).body(studentCardDTOs);
    }

    @PostMapping(path = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentDTO> create(
            @RequestPart("avatarFile") MultipartFile multipartFile,
            @RequestPart(value = "profileData") String profileData
    ) throws JsonProcessingException {
        AddStudentReq addStudentReq = objectMapper.readValue(profileData, AddStudentReq.class);

        StudentDTO studentDTO = studentService.create(multipartFile, addStudentReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @PutMapping(path = "update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<StudentDTO> update(
            @RequestPart("id") UUID id,
            @RequestPart("avatarFile") MultipartFile multipartFile,
            @RequestPart(value = "profileData") String profileData
    ) throws JsonProcessingException {
        UpdateStudentReq updateStudentReq = objectMapper.readValue(profileData, UpdateStudentReq.class);

        StudentDTO studentDTO = studentService.update(id, multipartFile, updateStudentReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(studentDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<StudentDTO> deleteById(
            @RequestParam UUID id
    ) {
        StudentDTO studentDTO = studentService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(studentDTO);
    }
}
