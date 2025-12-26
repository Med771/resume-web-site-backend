package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyFilterReq;

import ru.ai.sin.dto.company.UpdateCompanyReq;
import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.repository.CompanyRepo;
import ru.ai.sin.service.impl.CompanyService;

import ru.ai.sin.service.tools.CompanyTools;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    private final CompanyTools companyTools;

    private final SecurityHelper securityHelper;

    @Override
    @Transactional(readOnly = true)
    public CompanyDTO getById(long id) {
        return companyTools.mapToDTO(companyTools.getCompanyOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<CompanyDTO> getAllByFilter(
            Pageable pageable,
            CompanyFilterReq companyFilterReq
    ) {
        Page<CompanyEnt> companies = companyRepo
                .findAllByNameIgnoreCase(
                        companyFilterReq.name(),
                        pageable);

        List<CompanyEnt> companyEntList = companies.getContent();

        return new PageResponse<>(
                companyTools.mapToDTOs(companyEntList),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                companies.getTotalElements(),
                companies.getTotalPages());
    }

    @Override
    public CompanyDTO create(AddCompanyReq addCompanyReq) {
        CompanyDTO companyDTO = companyTools.newObjMapToDTO(companyRepo.save(new CompanyEnt(addCompanyReq.name())));

        log.info("User: {}, created a new company: {}", securityHelper.getCurrentUsername(), companyDTO);

        return companyDTO;
    }

    @Override
    @Transactional
    public CompanyDTO updateById(
            long id,
            UpdateCompanyReq updateCompanyReq
    ) {
        CompanyEnt companyEnt = companyTools.getCompanyOrThrow(id);

        companyEnt.setName(updateCompanyReq.name());

        CompanyDTO companyDTO = companyTools.mapToDTO(companyEnt);

        log.info("User: {}, update a company: {} with data: {}", securityHelper.getCurrentUsername(), id, companyDTO);

        return companyDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        CompanyEnt companyEnt = companyTools.getCompanyOrThrow(id);

        try {
            companyRepo.delete(companyEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting company: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting company");
        }

        log.info("User: {}, delete a company: {} with data: {}", securityHelper.getCurrentUsername(), id, companyEnt);
    }
}
