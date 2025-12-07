package ru.ai.sin.tools;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.ai.sin.entity.ExperienceEnt;
import ru.ai.sin.repository.ExperienceRepo;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExperienceTools {

    private final ExperienceRepo experienceRepo;

    @Transactional
    public List<Long> getExperienceIdsByCompanyId(Long companyId){
        List<ExperienceEnt> experienceEntList = experienceRepo.findAllByCompanyId(companyId);

        return experienceEntList.stream().map(ExperienceEnt::getId).toList();
    }
}
