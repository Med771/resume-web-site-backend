package ru.ai.sin.tools;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.company.CompanyDTO;

import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.entity.ExperienceEnt;

import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.CompanyMapper;
import ru.ai.sin.repository.CompanyRepo;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompanyTools {

    private final CompanyRepo companyRepo;

    private final CompanyMapper companyMapper;

    @Transactional(readOnly = true)
    public CompanyEnt getCompany(long id) {
        return companyRepo.findWithExperiencesById(id).orElseThrow(
                () -> new NotFoundException("Failed to find company with id " + id)
        );
    }

    public CompanyDTO mapToDTO(CompanyEnt companyEnt) {
        List<Long> experienceIds = companyEnt.getExperiences().stream().map(ExperienceEnt::getId).toList();

        return companyMapper.toDTO(companyEnt, experienceIds);
    }

    public List<CompanyDTO> mapToDTOs(List<CompanyEnt> companyEntList) {
        return companyEntList.stream()
                .map(companyEnt ->
                        companyMapper.toDTO(
                                companyEnt,
                                companyEnt.getExperiences().stream()
                                        .map(ExperienceEnt::getId)
                                        .toList())
                )
                .toList();
    }
}
