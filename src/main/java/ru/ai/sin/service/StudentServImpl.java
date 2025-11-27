package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ai.sin.dto.student.*;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.StudentService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServImpl implements StudentService {

    private final StudentRepo studentRepo;

    @Override
    public StudentDTO getById(UUID id) {
        return null;
    }

    @Override
    public List<StudentCardDTO> getAllCards(long page, long size) {
        return List.of();
    }

    @Override
    public List<StudentCardDTO> getAllByFilters(long page, long size, GetStudentFilterReq getStudentFilterReq) {
        return List.of();
    }

    @Override
    public List<StudentDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public StudentDTO create(MultipartFile multipartFile, AddStudentReq addStudentReq) {
        return null;
    }

    @Override
    public StudentDTO update(UUID id, MultipartFile multipartFile, UpdateStudentReq updateStudentReq) {
        return null;
    }


    @Override
    public StudentDTO deleteById(UUID id) {
        return null;
    }
}
