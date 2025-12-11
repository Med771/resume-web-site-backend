package ru.ai.sin.service.impl;

import ru.ai.sin.dto.institution.AddInstitutionReq;
import ru.ai.sin.dto.institution.GetAboutStudentRes;
import ru.ai.sin.dto.institution.GetAboutEducationRes;
import ru.ai.sin.dto.institution.InstitutionDTO;

import java.util.List;
import java.util.UUID;

public interface InstitutionService {

    // ---------- GET METHODS ----------
    InstitutionDTO getById(
            long id);

    GetAboutEducationRes getByEducationId(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize);
    GetAboutStudentRes getByStudentId(
            UUID id,
            int pageInstitutionNumber, int pageInstitutionSize);

    List<InstitutionDTO> getAll(
            int pageInstitutionNumber, int pageInstitutionSize);

    // ---------- POST METHODS ----------
    InstitutionDTO create(
            AddInstitutionReq addInstitutionReq);
    InstitutionDTO update(
            long id,
            AddInstitutionReq addInstitutionReq);

    // ---------- DELETE METHODS ----------
    InstitutionDTO deleteById(long id);
}
