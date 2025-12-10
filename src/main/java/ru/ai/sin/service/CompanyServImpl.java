package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.GetCompanyNameReq;

import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.exception.models.BadRequestException;
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

    @Override
    @Transactional
    public CompanyDTO getById(long id) {
        return companyTools.mapToDTO(companyTools.getCompanyOrThrow(id));
    }

    @Override
    public List<CompanyDTO> getAll(
            int pageCompanyNumber,
            int pageCompanySize
    ) {
        List<CompanyEnt> companyEntList = companyRepo
                .findAll(
                        PageRequest.of(pageCompanyNumber, pageCompanySize))
                .getContent();

        return companyTools.mapToDTOs(companyEntList);
    }

    @Override
    public List<CompanyDTO> getAllByName(
            int pageCompanyNumber,
            int pageCompanySize,
            GetCompanyNameReq getCompanyNameReq
    ) {
        List<CompanyEnt> companyEntList = companyRepo
                .findAllByNameIgnoreCase(
                        getCompanyNameReq.name(),
                        PageRequest.of(pageCompanyNumber, pageCompanySize))
                .getContent();

        return companyTools.mapToDTOs(companyEntList);
    }

    @Override
    public CompanyDTO create(AddCompanyReq addCompanyReq) {
        return companyTools.mapToDTO(companyRepo.save(new CompanyEnt(addCompanyReq.name())));
    }

    @Override
    @Transactional
    public CompanyDTO setNameById(
            long id,
            GetCompanyNameReq getCompanyNameReq
    ) {
        CompanyEnt companyEnt = companyTools.getCompanyOrThrow(id);

        companyEnt.setName(getCompanyNameReq.name());

        return companyTools.mapToDTO(companyEnt);
    }

    @Override
    @Transactional
    public CompanyDTO deleteById(long id) {
        CompanyEnt companyEnt = companyTools.getCompanyOrThrow(id);

        try {
            companyRepo.delete(companyEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting company: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting company");
        }

        return companyTools.mapToDTO(companyEnt);
    }
}
