package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.recruiter.*;

import java.util.UUID;

public interface RecruiterService {

    // ---------- GET METHODS ----------
    RecruiterDTO getById(UUID id);

    // ---------- POST METHODS ----------
    PageResponse<RecruiterDTO> getAllByFilter(
            Pageable pageable,
            RecruiterFilterReq recruiterFilterReq);

    // ---------- PUT METHODS ----------
    RecruiterDTO update(
            UUID id,
            UpdateRecruiterReq updateRecruiterReq);

    // ---------- DELETE METHODS ----------
    void deleteById(UUID id);
}
