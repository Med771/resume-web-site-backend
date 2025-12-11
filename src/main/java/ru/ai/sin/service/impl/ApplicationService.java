package ru.ai.sin.service.impl;

import ru.ai.sin.dto.application.*;

import java.util.List;
import java.util.UUID;

public interface ApplicationService {

    // ---------- GET METHODS ----------
    ApplicationDTO getById(long id);

    List<ApplicationDTO> getAll(long page, long size, GetApplicationFilterReq getApplicationFilterReq);

    GetHistoryRes getHistoryById(long id);

    // ---------- POST METHODS ----------
    ApplicationDTO create(AddApplicationReq  addApplicationReq);

    ApplicationDTO setChatIdById(long id, SetChatIdReq setChatIdReq);
    ApplicationDTO setResultById(long id, SetResultReq setResultReq);
    ApplicationDTO setHistoryById(long id, SetHistoryReq setHistoryReq);

    // ---------- DELETE METHODS ----------
    ApplicationDTO deleteById(long id);

    ApplicationDTO deleteByRecruitId(UUID recruitId);
    ApplicationDTO deleteByStudentId(UUID studentId);
}
