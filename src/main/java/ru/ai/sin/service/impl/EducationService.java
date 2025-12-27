package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.education.*;

public interface EducationService {

    // ---------- GET METHODS ----------
    EducationDTO getById(long id);

    PageResponse<EducationDTO> getAll(Pageable pageable);

    // ---------- POST METHODS ----------
    EducationDTO create(AddEducationReq addEducationReq);

    EducationDTO update(
            long id,
            UpdateEducationReq updateEducationReq);

    // ---------- DELETE METHODS ----------
    void deleteById(long id);
}
