package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import ru.ai.sin.entity.SpecialityEnt;

import ru.ai.sin.exception.models.NotFoundException;

import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.mapper.SpecialityMapper;

import ru.ai.sin.repository.SpecialityRepo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpecialityTools {

    private final SpecialityRepo specialityRepo;

    private final SpecialityMapper specialityMapper;
    private final SkillMapper skillMapper;

    @Transactional(readOnly = true)
    public SpecialityEnt getSpecialityOrThrow(long specialityId) {
        return specialityRepo.findById(specialityId).orElseThrow(
                () -> new NotFoundException("Failed to find special by id " + specialityId)
        );
    }

    @Transactional(readOnly = true)
    public SpecialityDTO mapToDto(SpecialityEnt specialityEnt) {
        List<SkillDTO> skillDTOs = specialityEnt
                .getSkills().stream()
                .map(skillMapper::toDTO)
                .toList();

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }
}
