package ru.ai.sin.service.impl;

import ru.ai.sin.dto.education.*;

import java.util.List;

public interface EducationService {

    // ---------- GET METHODS ----------
    EducationDTO getById(long id);

    List<EducationDTO> getAll(int page, int size);

    // ---------- POST METHODS ----------
    EducationDTO create(AddEducationReq addEducationReq);

    EducationDTO setInstitutionById(long id, SetEducationInstitutionReq setEducationInstitutionReq);
    EducationDTO setAdditionalInfoById(long id, SetEducationInfoReq setEducationInfoReq);
    EducationDTO setSkillsById(long id, SetEducationSkillsReq setEducationSkillsReq);

    // ---------- DELETE METHODS ----------
    EducationDTO deleteById(long id);
}
