package ru.ai.sin.service.impl;

import ru.ai.sin.dto.experience.AddExperienceReq;
import ru.ai.sin.dto.experience.GetAboutCompanyRes;
import ru.ai.sin.dto.experience.GetAboutStudentRes;
import ru.ai.sin.dto.experience.ExperienceDTO;

import java.util.List;
import java.util.UUID;

public interface ExperienceService {

    // ---------- GET METHODS ----------
    ExperienceDTO getById(
            long id
    );

    GetAboutCompanyRes getAboutCompanyById(
            long id,
            int pageExperienceNumber, int pageExperienceSize);
    GetAboutStudentRes getAboutStudentById(
            UUID id,
            int pageExperienceNumber, int pageExperienceSize);

    List<ExperienceDTO> getAll(
            int pageExperienceNumber, int pageExperienceSize);

    // ---------- POST METHODS ----------
    ExperienceDTO create(
            AddExperienceReq addExperienceReq);
    ExperienceDTO update(
            long id,
            AddExperienceReq addExperienceReq);

    // ---------- DELETE METHODS ----------
    ExperienceDTO deleteById(
            long id);
}
