package ru.ai.sin.service.impl;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SetSpecialityNameReq;
import ru.ai.sin.dto.speciality.SetSpecialitySkillsReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import java.util.List;

public interface SpecialityService {

    // ---------- GET METHODS ----------
    SpecialityDTO getById(Long id);

    List<SpecialityDTO> getAll(long page, long size);

    // ---------- POST METHODS ----------
    SpecialityDTO create(AddSpecialityReq addSpecialityReq);

    SpecialityDTO setNameById(Long id, SetSpecialityNameReq  setSpecialityNameReq);
    SpecialityDTO setSkillsById(Long id, SetSpecialitySkillsReq setSpecialitySkillsReq);

    // ---------- POST METHODS ----------
    SpecialityDTO deleteById(Long id);
}
