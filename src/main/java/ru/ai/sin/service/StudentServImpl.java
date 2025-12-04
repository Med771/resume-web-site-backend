package ru.ai.sin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.student.*;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.SpecialityEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.exception.models.BadRequestException;
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

    @Override
    @Transactional
    public StudentDTO getById(
            UUID id
    ) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        if (studentEnt == null) {
            throw new BadRequestException("Failed to find student by id " + id);
        }

        List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                .stream().map(skillMapper::toDTO).toList();

        return studentMapper.toDTO(studentEnt, skillDTOList);
    }

    @Override
    @Transactional
    public List<StudentCardDTO> getAllCards(
            int pageStudentNumber, int pageStudentSize
    ) {
        Pageable pageable = PageRequest.of(pageStudentNumber, pageStudentSize);

        List<StudentEnt> studentEntList = studentRepo.findAllByIsActiveTrue(pageable).getContent();

        return studentEntList.stream()
                .map(studentEnt -> {
                    List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                            .stream().map(skillMapper::toDTO).toList();

                    return studentMapper.toCardDTO(studentEnt, skillDTOList);
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
                    List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                            .stream().map(skillMapper::toDTO).toList();

                    return studentMapper.toDTO(studentEnt, skillDTOList);
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

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addStudentReq.skillsIds());

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.getSkills().addAll(skillEntSet);

        studentEnt = studentRepo.save(studentEnt);

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath != null) {
            studentEnt.setImagePath(filePath);

            studentRepo.save(studentEnt);

            List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                    .stream().map(skillMapper::toDTO).toList();

            return studentMapper.toDTO(studentEnt, skillDTOList);
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

        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(updateStudentReq.specialityId());

        if (specialityEnt == null) {
            throw new BadRequestException("Failed to find speciality with id " + updateStudentReq.specialityId());
        }

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(updateStudentReq.skillsIds());

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.getSkills().addAll(skillEntSet);

        studentMapper.updateEntityFromDto(updateStudentReq, studentEnt);

        String filePath = fileHelper.saveFile(multipartFile, studentEnt.getId().toString());

        if (filePath != null) {
            studentEnt.setImagePath(filePath);

            studentRepo.save(studentEnt);

            List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                    .stream().map(skillMapper::toDTO).toList();

            return studentMapper.toDTO(studentEnt, skillDTOList);
        }

        throw new BadRequestException("Failed to save student with id " + studentEnt.getId());
    }

    @Override
    @Transactional
    public StudentDTO deleteById(
            UUID id
    ) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        studentEnt.setIsActive(false);

        studentRepo.save(studentEnt);

        List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                .stream().map(skillMapper::toDTO).toList();

        return studentMapper.toDTO(studentEnt, skillDTOList);
    }
}
