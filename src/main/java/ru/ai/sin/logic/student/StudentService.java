package ru.ai.sin.logic.student;

import org.springframework.data.domain.Pageable;

import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.models.PageResponse;
import ru.ai.sin.logic.student.dto.*;

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
            FilterStudentReq filterStudentReq);
    PageResponse<StudentDTO> getAllByFilter(
            Pageable pageable,
            FilterStudentReq filterStudentReq);

    StudentDTO create(AddStudentReq addStudentReq);
    StudentDTO createExtended(CreateStudentExtendedReq createStudentExtendedReq);

    StudentDTO update(
            UUID id,
            UpdateStudentReq updateStudentReq);
    StudentDTO patch(
            UUID id,
            PatchStudentReq patchStudentReq);

    // ---------- DELETE METHODS ----------
    void deleteById(UUID id);
}
