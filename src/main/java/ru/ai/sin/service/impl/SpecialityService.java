package ru.ai.sin.service.impl;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import java.util.List;

public interface SpecialityService {

    // ---------- GET METHODS ----------
    SpecialityDTO getById(
            long id);

    List<SpecialityDTO> getAll(
            int pageSpecialityNumber, int pageSpecialitySize);

    // ---------- POST METHODS ----------
    SpecialityDTO create(
            AddSpecialityReq addSpecialityReq);

    SpecialityDTO update(
            long id,
            AddSpecialityReq addSpecialityReq);

    // ---------- POST METHODS ----------
    SpecialityDTO deleteById(
            long id);
}
