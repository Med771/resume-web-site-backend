package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterNameReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;
import ru.ai.sin.repository.RecruiterRepo;
import ru.ai.sin.service.impl.RecruiterService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterServImpl implements RecruiterService {

    private final RecruiterRepo  recruiterRepo;

    @Override
    public RecruiterDTO getById(UUID id) {
        return null;
    }

    @Override
    public List<RecruiterDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public List<RecruiterDTO> getAllByName(long page, long size, GetRecruiterNameReq getRecruiterNameReq) {
        return List.of();
    }

    @Override
    public RecruiterDTO create(AddRecruiterReq addRecruiterReq) {
        return null;
    }

    @Override
    public RecruiterDTO update(UUID id, UpdateRecruiterReq updateRecruiterReq) {
        return null;
    }

    @Override
    public RecruiterDTO deleteById(UUID id) {
        return null;
    }
}
