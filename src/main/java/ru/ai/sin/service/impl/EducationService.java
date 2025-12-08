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
            AddEducationReq addEducationReq,
            int pageInstitutionNumber, int pageInstitutionSize);

    EducationDTO update(
            long id,
            AddEducationReq addEducationReq,
            int pageInstitutionNumber, int pageInstitutionSize);

    // ---------- DELETE METHODS ----------
    EducationDTO deleteById(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize);
}
