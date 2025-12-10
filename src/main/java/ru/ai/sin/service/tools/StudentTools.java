package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.student.StudentDTO;

import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.exception.models.NotFoundException;

import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.mapper.StudentMapper;

import ru.ai.sin.repository.StudentRepo;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class StudentTools {

    private final StudentRepo studentRepo;

    private final StudentMapper studentMapper;
    private final SkillMapper skillMapper;

    @Transactional(readOnly = true)
    public StudentEnt getStudentOrThrow(UUID studentId) {
        return studentRepo.findById(studentId).orElseThrow(
                () -> new NotFoundException("Failed to find student by id " + studentId)
        );
    }

    public StudentDTO mapToDTO(StudentEnt studentEnt) {
        List<SkillDTO> skillDTOList = studentRepo.findSkillsByStudentId(studentEnt.getId())
                .stream().map(skillMapper::toDTO).toList();

        return studentMapper.toDTO(studentEnt, skillDTOList);
    }
}
