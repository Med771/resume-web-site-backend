package ru.ai.sin.service.impl;

import ru.ai.sin.dto.education.*;

import java.util.List;

public interface EducationService {

    // ---------- GET METHODS ----------
    EducationDTO getById(
            long id);

    List<EducationDTO> getAll(
            int pageEducationNumber, int pageEducationSize);

    // ---------- POST METHODS ----------
    EducationDTO create(
            AddEducationReq addEducationReq);

    EducationDTO update(
            long id,
            AddEducationReq addEducationReq);

    // ---------- DELETE METHODS ----------
    EducationDTO deleteById(
            long id);
}
