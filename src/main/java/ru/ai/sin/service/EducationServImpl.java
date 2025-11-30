package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.education.*;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.EducationEnt;
import ru.ai.sin.entity.InstitutionEnt;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.EducationMapper;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.repository.EducationRepo;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.service.impl.EducationService;

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

    @Override
    public EducationDTO getById(long id) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        List<Long> institutionsIds = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();
        List<SkillDTO> skillsIds = educationEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return educationMapper.toDTO(educationEnt, institutionsIds, skillsIds);
    }

    @Override
    public List<EducationDTO> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<EducationEnt> list = educationRepo.findAllByIsActiveTrue(pageRequest);

        return list.stream()
                .map( educationEnt -> {
                    List<Long> institutionsIds = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();
                    List<SkillDTO> skillDTOs = educationEnt.getSkills().stream()
                            .map(skillMapper::toDTO)
                            .toList();

                    return educationMapper.toDTO(educationEnt, institutionsIds, skillDTOs);
                })
                .toList();
    }

    @Override
    @Transactional
    public EducationDTO create(AddEducationReq addEducationReq) {
        EducationEnt educationEnt;

        EducationEnt optEducationEnt = educationRepo.findByInstitutionIgnoreCase(addEducationReq.institution());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addEducationReq.skillsIds());
        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();

        if (optEducationEnt !=  null) {
            educationEnt = optEducationEnt;

            educationEnt.setAdditionalInfo(addEducationReq.additionalInfo());
            educationEnt.setIsActive(true);
        }
        else {
            educationEnt = educationMapper.toEntity(addEducationReq);
        }

        List<Long> institutionsIds = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();

        educationEnt.setSkills(skillEntSet);

        educationEnt = educationRepo.save(educationEnt);

        return educationMapper.toDTO(educationEnt, institutionsIds, skillDTOs);
    }

    @Override
    @Transactional
    public EducationDTO setInstitutionById(long id, SetEducationInstitutionReq setEducationInstitutionReq) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        educationEnt.setInstitution(setEducationInstitutionReq.institution());

        educationEnt = educationRepo.save(educationEnt);

        List<Long> institutionDTOs = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();
        List<SkillDTO> skillDTOs = educationEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return educationMapper.toDTO(educationEnt, institutionDTOs, skillDTOs);
    }

    @Override
    @Transactional
    public EducationDTO setAdditionalInfoById(long id, SetEducationInfoReq setEducationInfoReq) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        educationEnt.setAdditionalInfo(setEducationInfoReq.additionalInfo());

        educationEnt = educationRepo.save(educationEnt);

        List<Long> institutionDTOs = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();
        List<SkillDTO> skillDTOs = educationEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return educationMapper.toDTO(educationEnt, institutionDTOs, skillDTOs);
    }

    @Override
    @Transactional
    public EducationDTO setSkillsById(long id, SetEducationSkillsReq setEducationSkillsReq) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        List<Long> institutionDTOs = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(setEducationSkillsReq.skillsIds());
        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();

        educationEnt.setSkills(skillEntSet);

        educationEnt = educationRepo.save(educationEnt);

        return educationMapper.toDTO(educationEnt, institutionDTOs, skillDTOs);
    }

    @Override
    @Transactional
    public EducationDTO deleteById(long id) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(id);

        if (educationEnt == null) {
            throw new BadRequestException("Failed find by id");
        }

        educationEnt.setIsActive(false);

        educationEnt = educationRepo.save(educationEnt);

        List<Long> institutionDTOs = educationEnt.getInstitutions().stream().map(InstitutionEnt::getId).toList();
        List<SkillDTO> skillDTOs = educationEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return educationMapper.toDTO(educationEnt, institutionDTOs, skillDTOs);
    }
}
