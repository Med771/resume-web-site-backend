package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    private final CompanyMapper companyMapper;

    private final ExperienceTools experienceTools;

    private CompanyEnt getActiveCompanyOrThrow(long id) {
        CompanyEnt companyEnt = companyRepo.findByIdAndIsActiveTrue(id);

        if (companyEnt == null) {
            throw new BadRequestException("Failed to find company with id " + id);
        }

        return companyEnt;
    }

    private CompanyDTO mapToDTO(CompanyEnt companyEnt) {
        List<Long> experienceIds = experienceTools.getExperienceIdsByCompanyId(companyEnt.getId());

        return companyMapper.toDTO(companyEnt, experienceIds);
    }

    private List<CompanyDTO> mapToDTOs(List<CompanyEnt> companyEntList) {
        Set<Long> companyIdSet = companyEntList.stream().map(CompanyEnt::getId).collect(Collectors.toSet());
        Map<Long, List<Long>> experiencesIds = experienceTools.getExperienceIdsByExperienceId(companyIdSet);

        return companyEntList.stream()
                .map(companyEnt ->
                        companyMapper.toDTO(companyEnt, experiencesIds.getOrDefault(companyEnt.getId(), List.of()))
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO getById(
            long id
    ) {
        CompanyEnt companyEnt = getActiveCompanyOrThrow(id);

        return mapToDTO(companyEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAll(
            int pageCompanyNumber, int pageCompanySize
    ) {
        Pageable pageable = PageRequest.of(pageCompanyNumber, pageCompanySize);

        List<CompanyEnt> companyEntList = companyRepo.findAllByIsActiveTrue(pageable).getContent();

        return mapToDTOs(companyEntList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyDTO> getAllByName(
            int pageCompanyNumber, int pageCompanySize,
            GetCompanyNameReq getCompanyNameReq
    ) {
        Pageable pageable = PageRequest.of(pageCompanyNumber, pageCompanySize);

        List<CompanyEnt> companyEntList = companyRepo.findAllByNameIgnoreCaseAndIsActiveTrue(
                getCompanyNameReq.name(), pageable).getContent();

        return mapToDTOs(companyEntList);
    }

    @Override
    @Transactional
    public CompanyDTO create(
            AddCompanyReq addCompanyReq
    ) {
        CompanyEnt companyEnt = new CompanyEnt();
        companyEnt.setName(addCompanyReq.name());

        companyEnt = companyRepo.save(companyEnt);

        return mapToDTO(companyEnt);
    }

    @Override
    @Transactional
    public CompanyDTO setNameById(
            long id,
            GetCompanyNameReq getCompanyNameReq
    ) {
        CompanyEnt companyEnt = getActiveCompanyOrThrow(id);

        companyEnt.setName(getCompanyNameReq.name());

        return mapToDTO(companyEnt);
    }

    @Override
    @Transactional
    public CompanyDTO deleteById(
            long id
    ) {
        CompanyEnt companyEnt = getActiveCompanyOrThrow(id);

        companyEnt.setIsActive(false);

        return mapToDTO(companyEnt);
    }
}
