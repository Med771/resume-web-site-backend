package ru.ai.sin.service.impl;

import ru.ai.sin.dto.telegram.*;

import java.util.UUID;

public interface TelegramService {
    StudentTelegramDTO getStudentByTelegramUserId(String telegramUserId);
    RecruiterTelegramDTO getRecruiterByTelegramUserId(String telegramUserId);

    OffersDTO filter(String userId, OfferFilterReq offerFilterReq);

    StudentTelegramDTO setStudentTelegramUserId(UUID studentId, SetTelegramUserIdReq setTelegramUserIdReq);

    RecruiterTelegramDTO setRecruiterTelegramUserId(UUID recruiterId, SetTelegramUserIdReq setTelegramUserIdReq);

    StudentTelegramDTO clearStudentTelegramUserId(UUID studentId);

    RecruiterTelegramDTO clearRecruiterTelegramUserId(UUID recruiterId);
}

