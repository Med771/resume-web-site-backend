package ru.ai.sin.logic.recruiter;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.recruiter.dto.*;

import java.util.UUID;

public interface RecruiterService {

    // ---------- GET METHODS ----------
    RecruiterDTO getById(UUID id);

    // ---------- POST METHODS ----------
    PageResponse<RecruiterDTO> getAllByFilter(
            Pageable pageable,
            FilterRecruiterReq filterRecruiterReq);

    // ---------- PUT METHODS ----------
    RecruiterDTO update(
            UUID id,
            UpdateRecruiterReq updateRecruiterReq);

    // ---------- DELETE METHODS ----------
    void deleteById(UUID id);
}
