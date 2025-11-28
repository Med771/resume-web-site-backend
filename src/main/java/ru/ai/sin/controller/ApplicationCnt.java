package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.application.*;
import ru.ai.sin.service.impl.ApplicationService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/application")
public class ApplicationCnt {

    private final ApplicationService applicationService;

    @GetMapping(path = "/getById")
    public ResponseEntity<ApplicationDTO> getById(
            @RequestParam long id) {
        ApplicationDTO applicationDTO = applicationService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @GetMapping(path = "getHistoryById")
    public ResponseEntity<GetHistoryRes> getHistoryById(
            @RequestParam long id) {
        GetHistoryRes getHistoryRes = applicationService.getHistoryById(id);

        return ResponseEntity.status(HttpStatus.OK).body(getHistoryRes);
    }

    @PostMapping(path = "getAll")
    public ResponseEntity<List<ApplicationDTO>> getAll(
            @RequestParam(defaultValue = "0") long page,
            @RequestParam(defaultValue = "10") long size,
            @RequestBody GetApplicationFilterReq getApplicationFilterReq) {
        List<ApplicationDTO> applicationDTOList = applicationService.getAll(page, size, getApplicationFilterReq);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTOList);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<ApplicationDTO> create(
            @RequestBody AddApplicationReq applicationReq) {
        ApplicationDTO applicationDTO = applicationService.create(applicationReq);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @PutMapping(path = "/setChatIdById")
    public ResponseEntity<ApplicationDTO> setChatIdById(
            @RequestParam long id,
            @RequestBody SetChatIdReq setChatIdReq) {
        ApplicationDTO applicationDTO = applicationService.setChatIdById(id, setChatIdReq);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @PutMapping(path = "/setResultById")
    public ResponseEntity<ApplicationDTO> setResultById(
            @RequestParam long id,
            @RequestBody SetResultReq setResultReq) {
        ApplicationDTO applicationDTO = applicationService.setResultById(id, setResultReq);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @PutMapping(path = "/setHistoryById")
    public ResponseEntity<ApplicationDTO> setHistoryById(
            @RequestParam long id,
            @RequestBody SetHistoryReq setHistoryReq) {
        ApplicationDTO applicationDTO = applicationService.setHistoryById(id, setHistoryReq);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<ApplicationDTO> deleteById(
            @RequestParam long id) {
        ApplicationDTO applicationDTO = applicationService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @DeleteMapping(path = "/deleteByRecruitId")
    public ResponseEntity<ApplicationDTO> deleteByRecruitId(
            @RequestParam UUID id) {
        ApplicationDTO applicationDTO = applicationService.deleteByRecruitId(id);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }

    @DeleteMapping(path = "/deleteByStudentId")
    public ResponseEntity<ApplicationDTO> deleteByStudentId(
            @RequestParam UUID id) {
        ApplicationDTO applicationDTO = applicationService.deleteByStudentId(id);

        return ResponseEntity.status(HttpStatus.OK).body(applicationDTO);
    }
}
