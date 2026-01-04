package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.experience.*;

public interface ExperienceService {

    // ---------- GET METHODS ----------
    ExperienceDTO getById(
            long id
    );

    // ---------- POST METHODS ----------
    PageResponse<ExperienceDTO> getAllByFilter(
            Pageable pageable,
            ExperienceFilterReq experienceFilterReq);

    ExperienceDTO create(AddExperienceReq addExperienceReq);
    ExperienceDTO update(
            long id,
            UpdateExperienceReq updateExperienceReq);

    // ---------- DELETE METHODS ----------
    void deleteById(long id);
}
