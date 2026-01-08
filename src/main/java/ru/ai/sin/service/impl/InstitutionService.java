package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.institution.*;

public interface InstitutionService {

    // ---------- GET METHODS ----------
    InstitutionDTO getById(long id);

    // ---------- POST METHODS ----------
    PageResponse<InstitutionDTO> getAllByFilter(Pageable pageable, InstitutionFilterReq institutionFilterReq);

    InstitutionDTO create(AddInstitutionReq addInstitutionReq);
    InstitutionDTO update(
            long id,
            UpdateInstitutionReq updateInstitutionReq);

    // ---------- DELETE METHODS ----------
    void deleteById(long id);
}
