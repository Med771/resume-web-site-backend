package ru.ai.sin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SetSpecialityNameReq;
import ru.ai.sin.dto.speciality.SetSpecialitySkillsReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.SpecialityEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.mapper.SpecialityMapper;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.repository.SpecialityRepo;
import ru.ai.sin.service.impl.SpecialityService;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialityServImpl implements SpecialityService {

    private final SpecialityRepo specialityRepo;
    private final SkillRepo skillRepo;

    private final SpecialityMapper specialityMapper;
    private final SkillMapper skillMapper;

    private SpecialityEnt getActiveSpecialityOrThrow(long specialityId) {
        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(specialityId);

        if (specialityEnt == null) {
            throw new NotFoundException("Failed to find special by id " + specialityId);
        }

        return specialityEnt;
    }

    public SpecialityDTO mapToDto(SpecialityEnt specialityEnt) {
        List<SkillDTO> skillDTOs = specialityEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }

    @Override
    public SpecialityDTO getById(
            long id
    ) {
        SpecialityEnt specialityEnt = getActiveSpecialityOrThrow(id);

        return mapToDto(specialityEnt);
    }

    @Override
    public List<SpecialityDTO> getAll(
            int pageSpecialityNumber, int pageSpecialitySize
    ) {
        Pageable pageable = PageRequest.of(pageSpecialityNumber, pageSpecialitySize);

        List<SpecialityEnt> list = specialityRepo.findAllByIsActiveTrue(pageable).getContent();

        return list.stream()
                .map(this::mapToDto).toList();
    }

    @Override
    @Transactional
    public SpecialityDTO create(
            AddSpecialityReq addSpecialityReq
    ) {
        SpecialityEnt specialityEnt = specialityRepo.findByName(addSpecialityReq.name());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addSpecialityReq.skillsIds());

        if  (specialityEnt == null) {
            specialityEnt = specialityMapper.toEntity(addSpecialityReq);
        }
        else {
            specialityEnt.setName(addSpecialityReq.name());
            specialityEnt.setIsActive(true);
        }

        specialityEnt.setSkills(skillEntSet);

        specialityEnt = specialityRepo.save(specialityEnt);

        return mapToDto(specialityEnt);
    }

    @Override
    @Transactional
    public SpecialityDTO setNameById(
            long id,
            SetSpecialityNameReq setSpecialityNameReq
    ) {
        SpecialityEnt specialityEnt = getActiveSpecialityOrThrow(id);

        specialityEnt.setName(setSpecialityNameReq.name());

        return mapToDto(specialityEnt);
    }

    @Override
    @Transactional
    public SpecialityDTO setSkillsById(
            long id,
            SetSpecialitySkillsReq setSpecialitySkillsReq
    ) {
        SpecialityEnt specialityEnt = getActiveSpecialityOrThrow(id);

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(setSpecialitySkillsReq.skillsIds());

        specialityEnt.setSkills(skillEntSet);

        return mapToDto(specialityEnt);
    }

    @Override
    @Transactional
    public SpecialityDTO deleteById(
            long id
    ) {
        SpecialityEnt specialityEnt = getActiveSpecialityOrThrow(id);

        specialityEnt.setIsActive(false);

        return mapToDto(specialityEnt);
    }
}
