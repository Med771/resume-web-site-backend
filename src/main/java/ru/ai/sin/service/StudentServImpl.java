package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.student.*;

import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.SpecialityEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.model.ContactInformation;
import ru.ai.sin.entity.spec.StudentSpecifications;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.FileHelper;
import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.mapper.StudentMapper;

import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.repository.StudentRepo;

import ru.ai.sin.service.impl.StudentService;
import ru.ai.sin.service.tools.SkillTools;
import ru.ai.sin.service.tools.SpecialityTools;
import ru.ai.sin.service.tools.StudentTools;

import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final SkillRepo skillRepo;

    private final StudentMapper studentMapper;

    private final StudentTools studentTools;
    private final SpecialityTools specialityTools;
    private final SkillTools skillTools;

    private final FileHelper fileHelper;
    private final SecurityHelper securityHelper;

    @Override
    public StudentDTO getById(UUID id) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        return studentTools.mapToDTO(studentEnt);
    }

    @Override
    @Transactional
    public void setPhoto(UUID id, MultipartFile file) {
        fileHelper.validateMultipart(file);

        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        String filePath = fileHelper.saveFile(file, studentEnt.getId().toString());

        if (filePath == null) {
            throw new BadRequestException("Failed to save file");
        }

        studentEnt.setImagePath(filePath);
    }

    @Override
    @Transactional
    public PageResponse<StudentCardDTO> getAllCardsByFilter(
            Pageable pageable,
            StudentFilterReq studentFilterReq
    ) {
        Page<StudentEnt> page = studentRepo.findAll(
                StudentSpecifications.byFilters(studentFilterReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(studentTools::mapToCardDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    public PageResponse<StudentDTO> getAllByFilter(
            Pageable pageable,
            StudentFilterReq studentFilterReq
    ) {
        Page<StudentEnt> page = studentRepo.findAll(
                StudentSpecifications.byFilters(studentFilterReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(studentTools::mapToDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }


    @Override
    @Transactional
    public StudentDTO create(AddStudentReq addStudentReq) {
        StudentEnt studentEnt = studentMapper.toEntity(addStudentReq);

        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(addStudentReq.specialityId());

        Set<SkillEnt> skillEntSet = skillTools.getSkillsByIds(addStudentReq.skillsIds());

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);

        studentEnt = studentRepo.save(studentEnt);

        try {
            studentEnt = studentRepo.save(studentEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Student already exists: {}, {}", addStudentReq.email(), addStudentReq.telegramUsername());

            throw new BadRequestException("Student already exists: %s, %s"
                    .formatted(addStudentReq.email(), addStudentReq.telegramUsername()));
        }

        StudentDTO studentDTO = studentTools.mapToDTO(studentEnt);

        log.info("User: {}, created a new student: {}", securityHelper.getCurrentUsername(), studentDTO);

        return studentDTO;
    }

    @Override
    @Transactional
    public StudentDTO update(
            UUID id,
            UpdateStudentReq updateStudentReq
    ) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(updateStudentReq.specialityId());
        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(updateStudentReq.skillsIds());

        studentMapper.updateEntityFromDto(updateStudentReq, studentEnt);

        if (studentEnt.getContactInformation() == null) {
            studentEnt.setContactInformation(new ContactInformation());
        }

        studentEnt.getUserInformation().setFirstName(updateStudentReq.firstName());
        studentEnt.getUserInformation().setLastName(updateStudentReq.lastName());
        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);

        StudentDTO studentDTO = studentTools.mapToDTO(studentEnt);

        log.info("User: {}, updated a student: {} with data: {}", securityHelper.getCurrentUsername(), id, studentDTO);

        return studentDTO;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        try {
            studentRepo.delete(studentEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting student: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting student");
        }

        log.info("User: {}, deleted a student: {} with data: {}", securityHelper.getCurrentUsername(), id, studentTools.mapToDTO(studentEnt));
    }
}
