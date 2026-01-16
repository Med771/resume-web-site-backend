package ru.ai.sin.service.impl;

import ru.ai.sin.dto.telegram.*;

import java.util.UUID;

public interface TelegramService {
    StudentTelegramDTO getStudentByTelegramUserId(String telegramUserId);
    RecruiterTelegramDTO getRecruiterByTelegramUserId(String telegramUserId);

    OffersDTO.Offer getById(long id);

    OffersDTO getAllOffersByResult(OffersFilterReq offersFilterReq);
    OffersDTO filter(String userId, OfferFilterReq offerFilterReq);

    StudentTelegramDTO setStudentTelegramUserId(UUID studentId, SetTelegramUserIdReq setTelegramUserIdReq);
    RecruiterTelegramDTO setRecruiterTelegramUserId(UUID recruiterId, SetTelegramUserIdReq setTelegramUserIdReq);

    StudentTelegramDTO clearStudentTelegramUserId(UUID studentId);
    RecruiterTelegramDTO clearRecruiterTelegramUserId(UUID recruiterId);

    OffersDTO.Offer createChat(long id);

    OffersDTO batchStatus(StatusUpdateReq statusUpdateReq);
}

