package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;

import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyFilterReq;
import ru.ai.sin.dto.company.UpdateCompanyReq;


public interface CompanyService {

    // ---------- GET METHODS ----------
    CompanyDTO getById(long id);

    // ---------- POST METHODS ----------
    PageResponse<CompanyDTO> getAllByFilter(
            Pageable pageable,
            CompanyFilterReq companyFilterReq);

    CompanyDTO create(AddCompanyReq addCompanyReq);

    CompanyDTO updateById(
            long id,
            UpdateCompanyReq updateCompanyReq);

    // ---------- DELETE METHODS ----------
    void deleteById(long id);
}
