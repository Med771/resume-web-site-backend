package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.institution.AddInstitutionReq;
import ru.ai.sin.dto.institution.GetAboutEducationRes;
import ru.ai.sin.dto.institution.GetAboutStudentInstitutionRes;
import ru.ai.sin.dto.institution.GetInstitutionRes;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/admin/institution")
public class InstitutionCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetInstitutionRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutGetByEducationId")
    public ResponseEntity<GetAboutEducationRes> aboutGetByEducationId(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutGetByStudentId")
    public ResponseEntity<GetAboutStudentInstitutionRes> aboutGetByStudentId(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutEducationAll")
    public ResponseEntity<List<GetAboutEducationRes>> aboutGetEducationAll(
            @RequestParam long page,
            @RequestParam long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutStudentsAll")
    public ResponseEntity<List<GetAboutStudentInstitutionRes>> aboutGetStudentAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/createInstitution")
    public ResponseEntity<GetInstitutionRes> createInstitution(
            @RequestBody AddInstitutionReq institutionReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping(path = "/mergeInstitution")
    public ResponseEntity<GetInstitutionRes> mergeInstitution(
            @RequestParam(defaultValue = "-1") long id,
            @RequestBody AddInstitutionReq institutionReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetInstitutionRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteByEducationId")
    public ResponseEntity<?> deleteByEducationId(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteByStudentId")
    public ResponseEntity<?> deleteByStudentId(
            @RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
