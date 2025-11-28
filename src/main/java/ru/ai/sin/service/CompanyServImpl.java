package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyNameDTO;
import ru.ai.sin.repository.CompanyRepo;
import ru.ai.sin.service.impl.CompanyService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyServImpl implements CompanyService {

    private final CompanyRepo companyRepo;

    @Override
    public CompanyDTO getById(long id) {
        return null;
    }

    @Override
    public List<CompanyDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public List<CompanyDTO> getAllByName(CompanyNameDTO companyNameDTO) {
        return List.of();
    }

    @Override
    public CompanyDTO create(AddCompanyReq addCompanyReq) {
        return null;
    }

    @Override
    public CompanyDTO setNameById(long id, CompanyNameDTO companyNameDTO) {
        return null;
    }

    @Override
    public CompanyDTO deleteById(long id) {
        return null;
    }
}
