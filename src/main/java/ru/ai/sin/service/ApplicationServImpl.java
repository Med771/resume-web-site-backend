package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.application.*;
import ru.ai.sin.repository.ApplicationRepo;
import ru.ai.sin.service.impl.ApplicationService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationServImpl implements ApplicationService {

    private final ApplicationRepo applicationRepo;

    @Override
    public ApplicationDTO getById(long id) {
        return null;
    }

    @Override
    public List<ApplicationDTO> getAll(long page, long size, GetApplicationFilterReq getApplicationFilterReq) {
        return List.of();
    }

    @Override
    public GetHistoryRes getHistoryById(long id) {
        return null;
    }

    @Override
    public ApplicationDTO create(AddApplicationReq addApplicationReq) {
        return null;
    }

    @Override
    public ApplicationDTO setChatIdById(long id, SetChatIdReq setChatIdReq) {
        return null;
    }

    @Override
    public ApplicationDTO setResultById(long id, SetResultReq setResultReq) {
        return null;
    }

    @Override
    public ApplicationDTO setHistoryById(long id, SetHistoryReq setHistoryReq) {
        return null;
    }

    @Override
    public ApplicationDTO deleteById(long id) {
        return null;
    }

    @Override
    public ApplicationDTO deleteByRecruitId(UUID recruitId) {
        return null;
    }

    @Override
    public ApplicationDTO deleteByStudentId(UUID studentId) {
        return null;
    }
}
