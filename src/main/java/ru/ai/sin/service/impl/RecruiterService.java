package ru.ai.sin.service.impl;

import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterNameReq;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;

import java.util.List;
import java.util.UUID;

public interface RecruiterService {

    // ---------- GET METHODS ----------
    RecruiterDTO getById(
            UUID id);

    List<RecruiterDTO> getAll(
            int pageRecruiterNumber, int pageRecruiterSize);
    List<RecruiterDTO> getAllByCompanyName(
            int pageRecruiterNumber, int pageRecruiterSize,
            GetRecruiterNameReq getRecruiterNameReq);

    // ---------- POST METHODS ----------
    RecruiterDTO create(
            AddRecruiterReq addRecruiterReq);

    // ---------- PUT METHODS ----------
    RecruiterDTO update(
            UUID id,
            UpdateRecruiterReq updateRecruiterReq);

    // ---------- DELETE METHODS ----------
    RecruiterDTO deleteById(
            UUID id);
}
