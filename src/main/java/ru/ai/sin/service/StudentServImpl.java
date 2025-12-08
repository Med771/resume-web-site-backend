package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.helper.FileHelper;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.mapper.StudentMapper;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.repository.SpecialityRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.StudentService;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final SpecialityRepo specialityRepo;
    private final SkillRepo skillRepo;

    private final StudentMapper studentMapper;
    private final SkillMapper skillMapper;

    private final FileHelper fileHelper;

    private StudentEnt getActiveStudentOrThrow(UUID id) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        if (studentEnt == null) {
            throw new NotFoundException("Failed to find student by id " + id);
        }

        return studentEnt;
    }

    private SpecialityEnt getActiveSpecialityOrThrow(long id) {
        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(id);

        if (specialityEnt == null) {
            throw new NotFoundException("Failed to find special by id " + id);
        }

        return specialityEnt;
    }

    @Transactional
    protected StudentDTO mapToDTO(StudentEnt studentEnt) {
        List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                .stream().map(skillMapper::toDTO).toList();

        return studentMapper.toDTO(studentEnt, skillDTOList);
    }

    @Transactional
    protected StudentCardDTO mapToCardDTO(StudentEnt studentEnt) {
        List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                .stream().map(skillMapper::toDTO).toList();

        return studentMapper.toCardDTO(studentEnt, skillDTOList);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getById(
            UUID id
    ) {
        StudentEnt studentEnt = getActiveStudentOrThrow(id);

        return mapToDTO(studentEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCardDTO> getAllCards(
            int pageStudentNumber, int pageStudentSize
    ) {
        Pageable pageable = PageRequest.of(pageStudentNumber, pageStudentSize);

        List<StudentEnt> studentEntList = studentRepo.findAllByIsActiveTrue(pageable).getContent();

        return studentEntList.stream()
                .map(this::mapToCardDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentCardDTO> getAllByFilters(
            int pageStudentNumber, int pageStudentSize,
            GetStudentFilterReq getStudentFilterReq
    ) {
        Specification<StudentEnt> spec = StudentSpecifications
                .courseIn(getStudentFilterReq.course())
                .and(StudentSpecifications.busynessIn(getStudentFilterReq.busyness()))
                .and(StudentSpecifications.bornBefore(getStudentFilterReq.bornBefore()))
                .and(StudentSpecifications.bornAfter(getStudentFilterReq.bornAfter()))
                .and(StudentSpecifications.hasSkills(getStudentFilterReq.skillsIds()))
                .and(StudentSpecifications.hasSpecialities(getStudentFilterReq.specialitiesIds()));

        Pageable pageable = PageRequest.of(pageStudentNumber, pageStudentSize);

        return studentRepo.findAll(spec, pageable).stream()
                .map(this::mapToCardDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAll(
            int pageStudentNumber, int pageStudentSize
    ) {
        Pageable pageable = PageRequest.of(pageStudentNumber, pageStudentSize);

        List<StudentEnt> studentEntList = studentRepo.findAllByIsActiveTrue(pageable).getContent();

        return studentEntList.stream()
                .map(this::mapToDTO).toList();
    }

    @Override
    @Transactional
    public StudentDTO create(
            MultipartFile multipartFile,
            AddStudentReq addStudentReq) {
        fileHelper.validateMultipart(multipartFile);

        StudentEnt studentEnt = studentMapper.toEntity(addStudentReq);

        SpecialityEnt specialityEnt = getActiveSpecialityOrThrow(addStudentReq.specialityId());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addStudentReq.skillsIds());

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);

        studentEnt = studentRepo.save(studentEnt);

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath == null) {
            throw new BadRequestException("Failed to save file");
        }
        studentEnt.setImagePath(filePath);

        studentRepo.save(studentEnt);

        return mapToDTO(studentEnt);
    }

    @Override
    @Transactional
    public StudentDTO update(
            UUID id,
            MultipartFile multipartFile,
            UpdateStudentReq updateStudentReq
    ) {
        fileHelper.validateMultipart(multipartFile);
        
        StudentEnt studentEnt = getActiveStudentOrThrow(id);

        SpecialityEnt specialityEnt = getActiveSpecialityOrThrow(updateStudentReq.specialityId());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(updateStudentReq.skillsIds());

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath == null) {
            throw new BadRequestException("Failed to save file");
        }

        studentMapper.updateEntityFromDto(updateStudentReq, studentEnt);

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);
        studentEnt.setImagePath(filePath);

        return mapToDTO(studentEnt);
    }

    @Override
    @Transactional
    public StudentDTO deleteById(
            UUID id
    ) {
        StudentEnt studentEnt = getActiveStudentOrThrow(id);

        studentEnt.setIsActive(false);

        return mapToDTO(studentEnt);
    }
}
