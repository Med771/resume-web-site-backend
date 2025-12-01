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
import ru.ai.sin.exception.models.BadRequestException;
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

    @Override
    public SpecialityDTO getById(
            long id
    ) {
        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(id);

        if (specialityEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        List<SkillDTO> skillDTOs = specialityEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }

    @Override
    public List<SpecialityDTO> getAll(
            int pageSpecialityNumber, int pageSpecialitySize
    ) {
        Pageable pageable = PageRequest.of(pageSpecialityNumber, pageSpecialitySize);

        List<SpecialityEnt> list = specialityRepo.findAllByIsActiveTrue(pageable).getContent();

        return list.stream()
                .map(specialityEnt -> {
                    List<SkillDTO> skillDTOs = specialityEnt.getSkills().stream()
                            .map(skillMapper::toDTO)
                            .toList();

                    return specialityMapper.toDTO(specialityEnt, skillDTOs);
                }).toList();
    }

    @Override
    @Transactional
    public SpecialityDTO create(
            AddSpecialityReq addSpecialityReq
    ) {
        SpecialityEnt specialityEnt = specialityRepo.findByNameIgnoreCaseAndIsActiveTrue(addSpecialityReq.name());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addSpecialityReq.skillsIds());
        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();

        if  (specialityEnt == null) {
            specialityEnt = specialityMapper.toEntity(addSpecialityReq);
        }
        else {
            specialityEnt.setName(addSpecialityReq.name());
            specialityEnt.setIsActive(true);

        }

        specialityEnt.setSkills(skillEntSet);

        specialityRepo.save(specialityEnt);

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }

    @Override
    @Transactional
    public SpecialityDTO setNameById(
            long id,
            SetSpecialityNameReq setSpecialityNameReq
    ) {
        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(id);

        if (specialityEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        specialityEnt.setName(setSpecialityNameReq.name());

        specialityRepo.save(specialityEnt);

        List<SkillDTO> skillDTOs = specialityEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }

    @Override
    @Transactional
    public SpecialityDTO setSkillsById(
            long id,
            SetSpecialitySkillsReq setSpecialitySkillsReq
    ) {
        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(id);

        if (specialityEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(setSpecialitySkillsReq.skillsIds());
        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();

        specialityEnt.setSkills(skillEntSet);

        specialityRepo.save(specialityEnt);

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }

    @Override
    @Transactional
    public SpecialityDTO deleteById(
            long id
    ) {
        SpecialityEnt specialityEnt = specialityRepo.findByIdAndIsActiveTrue(id);

        if (specialityEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        specialityEnt.setIsActive(false);

        specialityRepo.save(specialityEnt);

        List<SkillDTO> skillDTOs = specialityEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return specialityMapper.toDTO(specialityEnt, skillDTOs);
    }
}
