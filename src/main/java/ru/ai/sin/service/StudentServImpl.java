package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.student.*;

import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.SpecialityEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.spec.StudentSpecifications;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.FileHelper;

import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.mapper.StudentMapper;

import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.repository.StudentRepo;

import ru.ai.sin.service.impl.StudentService;
import ru.ai.sin.service.tools.SkillTools;
import ru.ai.sin.service.tools.SpecialityTools;
import ru.ai.sin.service.tools.StudentTools;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final SkillRepo skillRepo;

    private final StudentMapper studentMapper;
    private final SkillMapper skillMapper;

    private final StudentTools studentTools;
    private final SpecialityTools specialityTools;
    private final SkillTools skillTools;

    private final FileHelper fileHelper;

    @Transactional
    protected StudentCardDTO mapToCardDTO(StudentEnt studentEnt) {
        List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                .stream().map(skillMapper::toDTO).toList();

        return studentMapper.toCardDTO(studentEnt, skillDTOList);
    }

    @Override
    public StudentDTO getById(UUID id) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        return studentTools.mapToDTO(studentEnt);
    }

    @Override
    public List<StudentCardDTO> getAllCards(
            int pageStudentNumber,
            int pageStudentSize
    ) {
        List<StudentEnt> studentEntList = studentRepo
                .findAll(
                        PageRequest.of(pageStudentNumber, pageStudentSize))
                .getContent();

        return studentEntList.stream()
                .map(this::mapToCardDTO).toList();
    }

    @Override
    public List<StudentCardDTO> getAllByFilters(
            int pageStudentNumber,
            int pageStudentSize,
            GetStudentFilterReq getStudentFilterReq
    ) {
        Specification<StudentEnt> spec = StudentSpecifications
                .courseIn(getStudentFilterReq.course())
                .and(StudentSpecifications.busynessIn(getStudentFilterReq.busyness()))
                .and(StudentSpecifications.bornBefore(getStudentFilterReq.bornBefore()))
                .and(StudentSpecifications.bornAfter(getStudentFilterReq.bornAfter()))
                .and(StudentSpecifications.hasSkills(getStudentFilterReq.skillsIds()))
                .and(StudentSpecifications.hasSpecialities(getStudentFilterReq.specialitiesIds()));

        return studentRepo
                .findAll(
                        spec,
                        PageRequest.of(pageStudentNumber, pageStudentSize))
                .stream()
                .map(this::mapToCardDTO).toList();
    }

    @Override
    public List<StudentDTO> getAll(
            int pageStudentNumber,
            int pageStudentSize
    ) {
        List<StudentEnt> studentEntList = studentRepo
                .findAll(
                        PageRequest.of(pageStudentNumber, pageStudentSize))
                .getContent();

        return studentEntList.stream()
                .map(studentTools::mapToDTO).toList();
    }

    @Override
    @Transactional
    public StudentDTO create(
            MultipartFile multipartFile,
            AddStudentReq addStudentReq
    ) {
        fileHelper.validateMultipart(multipartFile);

        StudentEnt studentEnt = studentMapper.toEntity(addStudentReq);

        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(addStudentReq.specialityId());

        Set<SkillEnt> skillEntSet = skillTools.getSkillsByIds(addStudentReq.skillsIds());

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);

        studentEnt = studentRepo.save(studentEnt);

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath == null) {
            throw new BadRequestException("Failed to save file");
        }

        studentEnt.setImagePath(filePath);

        try {
            studentEnt = studentRepo.save(studentEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Student already exists: {}, {}", addStudentReq.email(), addStudentReq.telegramUsername());

            throw new BadRequestException("Student already exists: %s, %s"
                    .formatted(addStudentReq.email(), addStudentReq.telegramUsername()));
        }

        return studentTools.mapToDTO(studentEnt);
    }

    @Override
    @Transactional
    public StudentDTO update(
            UUID id,
            MultipartFile multipartFile,
            UpdateStudentReq updateStudentReq
    ) {
        fileHelper.validateMultipart(multipartFile);
        
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(updateStudentReq.specialityId());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(updateStudentReq.skillsIds());

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath == null) {
            throw new BadRequestException("Failed to save file");
        }

        studentMapper.updateEntityFromDto(updateStudentReq, studentEnt);

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);
        studentEnt.setImagePath(filePath);

        return studentTools.mapToDTO(studentEnt);
    }

    @Override
    @Transactional
    public StudentDTO deleteById(UUID id) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        try {
            studentRepo.delete(studentEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting student: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting student");
        }

        return studentTools.mapToDTO(studentEnt);
    }
}
