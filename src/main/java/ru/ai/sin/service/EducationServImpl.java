package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.education.*;
import ru.ai.sin.dto.skill.SkillDTO;

import ru.ai.sin.entity.EducationEnt;
import ru.ai.sin.entity.SkillEnt;

import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.EducationMapper;
import ru.ai.sin.mapper.SkillMapper;

import ru.ai.sin.repository.EducationRepo;
import ru.ai.sin.repository.SkillRepo;

import ru.ai.sin.service.impl.EducationService;

import ru.ai.sin.tools.InstitutionTools;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationServImpl implements EducationService {

    private final EducationRepo educationRepo;
    private final SkillRepo skillRepo;

    private final EducationMapper educationMapper;
    private final SkillMapper skillMapper;

    private final InstitutionTools institutionTools;

    @Override
    @Transactional
    public EducationDTO getById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        List<Long> institutionsIds = institutionTools.getInstitutionIdsByEducationId(
                educationEnt.getId(),
                pageInstitutionNumber, pageInstitutionSize);
        List<SkillDTO> skillsIds = educationEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return educationMapper.toDTO(educationEnt, institutionsIds, skillsIds);
    }

    @Override
    @Transactional
    public List<EducationDTO> getAll(
            int pageEducationNumber, int pageEducationSize,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        Page<EducationEnt> educationPage = educationRepo.findAllByIsActiveTrue(
                PageRequest.of(pageEducationNumber, pageEducationSize));

        return educationPage.getContent().stream()
                .map( educationEnt -> {
                    List<Long> institutionsIds = institutionTools.getInstitutionIdsByEducationId(
                            educationEnt.getId(),
                            pageInstitutionNumber, pageInstitutionSize
                    );

                    List<SkillDTO> skillDTOs = educationEnt.getSkills().stream()
                            .map(skillMapper::toDTO)
                            .toList();

                    return educationMapper.toDTO(educationEnt, institutionsIds, skillDTOs);
                })
                .toList();
    }

    @Override
    @Transactional
    public EducationDTO create(
            AddEducationReq addEducationReq,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        EducationEnt educationEnt = educationRepo.findByInstitutionIgnoreCase(addEducationReq.institution());

        if (educationEnt != null) {
            educationEnt.setAdditionalInfo(addEducationReq.additionalInfo());
            educationEnt.setIsActive(true);
        }
        else {
            educationEnt = educationMapper.toEntity(addEducationReq);
        }

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addEducationReq.skillsIds());

        educationEnt.setSkills(skillEntSet);

        educationEnt = educationRepo.save(educationEnt);

        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();
        List<Long> institutionsIds = institutionTools.getInstitutionIdsByEducationId(
                educationEnt.getId(),
                pageInstitutionNumber, pageInstitutionSize
        );

        return educationMapper.toDTO(educationEnt, institutionsIds, skillDTOs);
    }

    @Override
    @Transactional
    public EducationDTO update(
            long id,
            AddEducationReq addEducationReq,
            int pageInstitutionNumber, int pageInstitutionSize) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed to find education by id: " + id);
        }

        educationEnt.setIsActive(true);

        educationMapper.updateEntityFromDto(addEducationReq, educationEnt);

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addEducationReq.skillsIds());

        educationEnt = educationRepo.save(educationEnt);

        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();
        List<Long> institutionsIds = institutionTools.getInstitutionIdsByEducationId(
                educationEnt.getId(),
                pageInstitutionNumber, pageInstitutionSize
        );

        return educationMapper.toDTO(educationEnt, institutionsIds, skillDTOs);
    }

    @Override
    @Transactional
    public EducationDTO deleteById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        educationEnt.setIsActive(false);

        educationRepo.save(educationEnt);

        List<SkillDTO> skillDTOs = educationEnt.getSkills().stream().map(skillMapper::toDTO).toList();
        List<Long> institutionsIds = institutionTools.getInstitutionIdsByEducationId(
                educationEnt.getId(),
                pageInstitutionNumber, pageInstitutionSize);

        return educationMapper.toDTO(educationEnt, institutionsIds, skillDTOs);
    }
}
