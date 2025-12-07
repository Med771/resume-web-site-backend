package ru.ai.sin.service.impl;


import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.dto.company.GetCompanyNameReq;

import java.util.List;

public interface CompanyService {

    // ---------- GET METHODS ----------
    CompanyDTO getById(
            long id);

    List<CompanyDTO> getAll(
            int pageCompanyNumber, int pageCompanySize);
    List<CompanyDTO> getAllByName(
            int pageCompanyNumber, int pageCompanySize,
            GetCompanyNameReq getCompanyNameReq);

    // ---------- POST METHODS ----------
    CompanyDTO create(
            AddCompanyReq addCompanyReq);

    CompanyDTO setNameById(
            long id,
            GetCompanyNameReq getCompanyNameReq);

    // ---------- DELETE METHODS ----------
    CompanyDTO deleteById(
            long id);
}
