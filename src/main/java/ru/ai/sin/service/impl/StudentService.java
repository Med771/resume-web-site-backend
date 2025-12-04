package ru.ai.sin.service.impl;

import org.springframework.web.multipart.MultipartFile;
import ru.ai.sin.dto.student.*;

import java.util.List;
import java.util.UUID;

public interface StudentService {

    // ---------- GET METHODS ----------
    StudentDTO getById(
            UUID id);

    List<StudentCardDTO> getAllCards(
            int pageStudentNumber, int pageStudentSize);
    List<StudentCardDTO> getAllByFilters(
            int pageStudentNumber, int pageStudentSize,
            GetStudentFilterReq getStudentFilterReq);
    List<StudentDTO> getAll(
            int pageStudentNumber, int pageStudentSize);

    // ---------- POST METHODS ----------
    StudentDTO create(
            MultipartFile multipartFile,
            AddStudentReq  addStudentReq);
    StudentDTO update(
            UUID id,
            MultipartFile multipartFile,
            UpdateStudentReq updateStudentReq);

    // ---------- DELETE METHODS ----------
    StudentDTO deleteById(
            UUID id);
}
