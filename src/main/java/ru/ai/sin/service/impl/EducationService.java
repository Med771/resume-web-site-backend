package ru.ai.sin.service.impl;

import ru.ai.sin.dto.education.*;

import java.util.List;

public interface EducationService {

    // ---------- GET METHODS ----------
    EducationDTO getById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize);

    List<EducationDTO> getAll(
            int pageEducationNumber, int pageEducationSize,
            int pageInstitutionNumber, int pageInstitutionSize);

    // ---------- POST METHODS ----------
    EducationDTO create(
            AddEducationReq addEducationReq);

    EducationDTO setInstitutionById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize,
            SetEducationInstitutionReq setEducationInstitutionReq);
    EducationDTO setAdditionalInfoById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize,
            SetEducationInfoReq setEducationInfoReq);
    EducationDTO setSkillsById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize,
            SetEducationSkillsReq setEducationSkillsReq);

    // ---------- DELETE METHODS ----------
    EducationDTO deleteById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize);
}
