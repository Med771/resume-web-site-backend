package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.student.*;

import java.util.UUID;

public interface StudentService {

    // ---------- GET METHODS ----------
    StudentDTO getById(UUID id);

    // ---------- POST METHODS ----------
    void setPhoto(
            UUID id,
            MultipartFile file);

    PageResponse<StudentCardDTO> getAllCardsByFilter(
            Pageable pageable,
            StudentFilterReq studentFilterReq);
    PageResponse<StudentDTO> getAllByFilter(
            Pageable pageable,
            StudentFilterReq studentFilterReq);

    StudentDTO create(AddStudentReq  addStudentReq);

    StudentDTO update(
            UUID id,
            UpdateStudentReq updateStudentReq);

    // ---------- DELETE METHODS ----------
    void deleteById(UUID id);
}
