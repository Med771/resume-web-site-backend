package ru.ai.sin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ai.sin.dto.student.*;
import ru.ai.sin.entity.SpecialityEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.helper.FileHelper;
import ru.ai.sin.mapper.StudentMapper;
import ru.ai.sin.repository.SpecialityRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.StudentService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final SpecialityRepo specialityRepo;

    private final StudentMapper studentMapper;

    private final FileHelper fileHelper;

    @Override
    public StudentDTO getById(
            UUID id
    ) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        if (studentEnt == null) {
            throw new BadRequestException("Failed to find student by id " + id);
        }

        // TODO: add all skills

        return studentMapper.toDTO(studentEnt, List.of());
    }

    @Override
    public List<StudentCardDTO> getAllCards(
            int pageStudentNumber, int pageStudentSize
    ) {
        Pageable pageable = PageRequest.of(pageStudentNumber, pageStudentSize);

        List<StudentEnt> studentEntList = studentRepo.findAllByIsActiveTrue(pageable).getContent();

        return studentEntList.stream()
                .map(studentEnt -> {
                    return studentMapper.toCardDTO(studentEnt, List.of());
                }).toList();
    }

    @Override
    public List<StudentCardDTO> getAllByFilters(
            int pageStudentNumber, int pageStudentSize,
            GetStudentFilterReq getStudentFilterReq
    ) {
        return List.of();
    }

    @Override
    public List<StudentDTO> getAll(
            int pageStudentNumber, int pageStudentSize
    ) {
        Pageable pageable = PageRequest.of(pageStudentNumber, pageStudentSize);

        List<StudentEnt> studentEntList = studentRepo.findAllByIsActiveTrue(pageable).getContent();

        return studentEntList.stream()
                .map(studentEnt -> {
                    return studentMapper.toDTO(studentEnt, List.of());
                }).toList();
    }

    @Override
    @Transactional
    public StudentDTO create(
            MultipartFile multipartFile,
            AddStudentReq addStudentReq) {
        StudentEnt studentEnt = studentMapper.toEntity(addStudentReq);

        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(addStudentReq.specialityId());

        if (specialityEnt == null) {
            throw new BadRequestException("Failed to find speciality with id " + addStudentReq.specialityId());
        }

        studentEnt.setSpeciality(specialityEnt);

        studentEnt = studentRepo.save(studentEnt);

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath != null) {
            studentEnt.setImagePath(filePath);

            studentRepo.save(studentEnt);

            return studentMapper.toDTO(studentEnt, List.of());
        }
        studentRepo.delete(studentEnt);

        throw new BadRequestException("Failed to save student with id " + studentEnt.getId());
    }

    @Override
    @Transactional
    public StudentDTO update(
            UUID id,
            MultipartFile multipartFile,
            UpdateStudentReq updateStudentReq
    ) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        // TODO: add logic

        studentRepo.save(studentEnt);

        return studentMapper.toDTO(studentEnt, List.of());
    }

    @Override
    @Transactional
    public StudentDTO deleteById(
            UUID id
    ) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        studentEnt.setIsActive(false);

        studentRepo.save(studentEnt);

        return studentMapper.toDTO(studentEnt, List.of());
    }
}
