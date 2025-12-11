package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.experience.ExperienceDTO;
import ru.ai.sin.dto.experience.ExperienceRes;
import ru.ai.sin.entity.ExperienceEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.ExperienceMapper;
import ru.ai.sin.repository.ExperienceRepo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceTools {

    private final ExperienceRepo experienceRepo;

    private final ExperienceMapper experienceMapper;

    @Transactional(readOnly = true)
    public ExperienceEnt getExperienceOrThrow(Long id) {
        ExperienceEnt experienceEnt = experienceRepo.findWithCompanyAndStudentById(id);

        if  (experienceEnt == null) {
            throw new NotFoundException("Failed to find experience by id " + id);
        }

        return experienceEnt;
    }

    @Transactional(readOnly = true)
    public List<Long> getExperienceIdsByCompanyId(Long companyId){
        List<ExperienceEnt> experienceEntList = experienceRepo.findAllByCompanyId(companyId);

        return experienceEntList.stream().map(ExperienceEnt::getId).toList();
    }

    @Transactional(readOnly = true)
    public Map<Long, List<Long>> getExperienceIdsByExperienceId(Set<Long> companyIds){
        Set<ExperienceEnt> experienceEntSet = experienceRepo.findAllByCompanyIdIn(companyIds);

        return experienceEntSet.stream()
                .collect(Collectors.groupingBy(
                        exp -> exp.getCompany().getId(),
                        Collectors.mapping(ExperienceEnt::getId, Collectors.toList())
                ));
    }

    public ExperienceDTO mapToDTO(ExperienceEnt experienceEnt) {
        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }
}
