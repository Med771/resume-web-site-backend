package ru.ai.sin.service.impl;

import ru.ai.sin.dto.telegram.RecruiterTelegramDTO;
import ru.ai.sin.dto.telegram.SetTelegramUserIdReq;
import ru.ai.sin.dto.telegram.StudentTelegramDTO;

import java.util.UUID;

public interface TelegramService {
    StudentTelegramDTO setStudentTelegramUserId(UUID studentId, SetTelegramUserIdReq setTelegramUserIdReq);

    RecruiterTelegramDTO setRecruiterTelegramUserId(UUID recruiterId, SetTelegramUserIdReq setTelegramUserIdReq);

    StudentTelegramDTO clearStudentTelegramUserId(UUID studentId);

    RecruiterTelegramDTO clearRecruiterTelegramUserId(UUID recruiterId);
}

