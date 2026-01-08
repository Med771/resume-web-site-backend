package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.speciality.*;


public interface SpecialityService {

    // ---------- GET METHODS ----------
    SpecialityDTO getById(
            long id);

    // ---------- POST METHODS ----------
    PageResponse<SpecialityDTO> getAllByFilter(Pageable pageable, SpecialityFilterReq specialityFilterReq);

    SpecialityDTO create(AddSpecialityReq addSpecialityReq);

    SpecialityDTO update(
            long id,
            UpdateSpecialityReq updateSpecialityReq);

    // ---------- POST METHODS ----------
    void deleteById(long id);
}
