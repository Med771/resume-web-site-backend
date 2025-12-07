package ru.ai.sin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.GetCompanyNameReq;
import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.CompanyMapper;
import ru.ai.sin.repository.CompanyRepo;
import ru.ai.sin.service.impl.CompanyService;
import ru.ai.sin.tools.ExperienceTools;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    private final CompanyMapper companyMapper;

    private final ExperienceTools experienceTools;

    @Override
    @Transactional
    public CompanyDTO getById(
            long id
    ) {
        CompanyEnt companyEnt = companyRepo.findByIdAndIsActiveTrue(id);

        if (companyEnt == null) {
            throw new BadRequestException("Failed to find company with id " + id);
        }

        List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(companyEnt.getId());

        return companyMapper.toDTO(companyEnt, experienceIds);
    }

    @Override
    @Transactional
    public List<CompanyDTO> getAll(
            int pageCompanyNumber, int pageCompanySize
    ) {
        Pageable pageable = PageRequest.of(pageCompanyNumber, pageCompanySize);

        List<CompanyEnt> companyEntList = companyRepo.findAllByIsActiveTrue(pageable).getContent();

        return companyEntList.stream()
                .map(companyEnt -> {
                    List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(companyEnt.getId());

                    return companyMapper.toDTO(companyEnt, experienceIds);
                }).toList();
    }

    @Override
    @Transactional
    public List<CompanyDTO> getAllByName(
            int pageCompanyNumber, int pageCompanySize,
            GetCompanyNameReq getCompanyNameReq
    ) {
        Pageable pageable = PageRequest.of(pageCompanyNumber, pageCompanySize);

        List<CompanyEnt> companyEntList = companyRepo.findAllByNameIgnoreCaseAndIsActiveTrue(
                getCompanyNameReq.name(), pageable).getContent();

        return companyEntList.stream()
                .map(companyEnt -> {
                    List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(companyEnt.getId());

                    return companyMapper.toDTO(companyEnt, experienceIds);
                }).toList();
    }

    @Override
    @Transactional
    public CompanyDTO create(
            AddCompanyReq addCompanyReq
    ) {
        CompanyEnt companyEnt = new CompanyEnt();
        companyEnt.setName(addCompanyReq.name());

        companyEnt = companyRepo.save(companyEnt);

        List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(companyEnt.getId());

        return companyMapper.toDTO(companyEnt, experienceIds);
    }

    @Override
    @Transactional
    public CompanyDTO setNameById(
            long id,
            GetCompanyNameReq getCompanyNameReq
    ) {
        CompanyEnt companyEnt = companyRepo.findByIdAndIsActiveTrue(id);

        if (companyEnt == null) {
            throw new BadRequestException("Failed to find company with id " + id);
        }

        companyEnt.setName(getCompanyNameReq.name());

        companyRepo.save(companyEnt);

        List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(id);

        return companyMapper.toDTO(companyEnt, experienceIds);
    }

    @Override
    @Transactional
    public CompanyDTO deleteById(
            long id
    ) {
        CompanyEnt companyEnt = companyRepo.findByIdAndIsActiveTrue(id);

        if (companyEnt == null) {
            throw new BadRequestException("Failed to find company with id " + id);
        }

        companyEnt.setIsActive(false);

        companyRepo.save(companyEnt);

        List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(id);

        return companyMapper.toDTO(companyEnt, experienceIds);
    }
}
