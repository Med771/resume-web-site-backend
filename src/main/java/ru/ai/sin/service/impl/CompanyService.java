package ru.ai.sin.service.impl;


import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.CompanyNameDTO;

import java.util.List;

public interface CompanyService {

    // ---------- GET METHODS ----------
    CompanyDTO getById(long id);

    List<CompanyDTO> getAll(long page, long size);
    List<CompanyDTO> getAllByName(CompanyNameDTO companyNameDTO);

    // ---------- POST METHODS ----------
    CompanyDTO create(AddCompanyReq addCompanyReq);

    CompanyDTO setNameById(long id,CompanyNameDTO companyNameDTO);

     // ---------- DELETE METHODS ----------
    CompanyDTO deleteById(long id);
}
