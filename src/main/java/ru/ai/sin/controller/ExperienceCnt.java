package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.experience.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/admin/experience")
public class ExperienceCnt {

    @GetMapping(path = "/aboutGetByCompanyId")
    public ResponseEntity<GetAboutCompanyRes> aboutGetByCompanyId(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutGetByStudentId")
    public ResponseEntity<GetAboutStudentRes> aboutGetByStudentId(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutCompanyAll")
    public ResponseEntity<List<GetAboutCompanyRes>> aboutGetCompanyAll(
            @RequestParam long page,
            @RequestParam long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/aboutStudentsAll")
    public ResponseEntity<List<GetStudentExperienceRes>> aboutGetStudentAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/createExperience")
    public ResponseEntity<AddExperienceReq> createExperience(
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PutMapping(path = "/mergeExperience")
    public ResponseEntity<AddExperienceReq> mergeExperience(
        @RequestParam(defaultValue = "-1") long id
        ) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetExperienceRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteByCompanyId")
    public ResponseEntity<GetCompanyExperienceRes> deleteByCompanyId(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteByStudentId")
    public ResponseEntity<GetStudentExperienceRes> deleteByStudentId(
            @RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
