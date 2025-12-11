package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.entity.SpecialityEnt;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.mapper.SpecialityMapper;

import ru.ai.sin.repository.SpecialityRepo;

import ru.ai.sin.service.impl.SpecialityService;
import ru.ai.sin.service.tools.SkillTools;
import ru.ai.sin.service.tools.SpecialityTools;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialityServImpl implements SpecialityService {

    private final SpecialityRepo specialityRepo;

    private final SpecialityMapper specialityMapper;

    private final SpecialityTools specialityTools;
    private final SkillTools skillTools;

    @Override
    public SpecialityDTO getById(long id) {
        return specialityTools.mapToDto(specialityTools.getSpecialityOrThrow(id));
    }

    @Override
    public List<SpecialityDTO> getAll(
            int pageSpecialityNumber,
            int pageSpecialitySize
    ) {
        List<SpecialityEnt> specialityEntList = specialityRepo
                .findAll(
                        PageRequest.of(pageSpecialityNumber, pageSpecialitySize))
                .getContent();

        return specialityEntList.stream()
                .map(specialityTools::mapToDto)
                .toList();
    }

    @Override
    @Transactional
    public SpecialityDTO create(AddSpecialityReq addSpecialityReq) {
        SpecialityEnt specialityEnt = specialityMapper.toEntity(addSpecialityReq);
        Set<SkillEnt> skillEntSet = skillTools.getSkillsByIds(addSpecialityReq.skillsIds());

        specialityEnt.setSkills(skillEntSet);

        try {
            specialityEnt = specialityRepo.save(specialityEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Speciality already exists: {}", addSpecialityReq.name());

            throw new BadRequestException("Speciality already exists: " + addSpecialityReq.name());
        }

        return specialityTools.mapToDto(specialityEnt);
    }


    @Override
    @Transactional
    public SpecialityDTO update(
            long id,
            AddSpecialityReq addSpecialityReq
    ) {
        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(id);
        Set<SkillEnt> skillEntSet = skillTools.getSkillsByIds(addSpecialityReq.skillsIds());

        specialityMapper.updateEntityFromDto(addSpecialityReq, specialityEnt);
        specialityEnt.setSkills(skillEntSet);

        return specialityTools.mapToDto(specialityEnt);
    }

    @Override
    @Transactional
    public SpecialityDTO deleteById(long id) {
        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(id);

        try {
            specialityRepo.delete(specialityEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting speciality: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting speciality");
        }

        return specialityTools.mapToDto(specialityEnt);
    }
}
