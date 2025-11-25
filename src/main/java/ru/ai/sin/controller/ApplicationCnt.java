package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.application.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/application")
public class ApplicationCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<ApplicationDTO> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getByRecruitId")
    public ResponseEntity<List<ApplicationDTO>> getByRecruitId(
            @RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getByStudentId")
    public ResponseEntity<List<ApplicationDTO>> getByStudentId(
            @RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "getAll")
    public ResponseEntity<List<ApplicationDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "getHistoryById")
    public ResponseEntity<?> getHistoryById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApplicationDTO> create(
            @RequestBody AddApplicationReq applicationReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/setChatIdById")
    public ResponseEntity<ApplicationDTO> setChatIdById(
            @RequestParam long id,
            @RequestBody SetChatIdReq setChatIdReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/setResultById")
    public ResponseEntity<ApplicationDTO> setResultById(
            @RequestParam long id,
            @RequestBody SetResultReq setResultReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/setHistoryById")
    public ResponseEntity<ApplicationDTO> setHistoryById(
            @RequestParam long id,
            @RequestBody SetHistoryReq setHistoryReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<ApplicationDTO> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteByRecruitId")
    public ResponseEntity<ApplicationDTO> deleteByRecruitId(
            @RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteByStudentId")
    public ResponseEntity<ApplicationDTO> deleteByStudentId(
            @RequestParam UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
