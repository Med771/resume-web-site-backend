package ru.ai.sin.service.impl;

import ru.ai.sin.dto.telegram.*;

public interface TelegramService {
    TelegramUserRes getByTelegramUserId(String telegramUserId);

    OffersDTO.Offer getById(long id);

    OffersDTO getAllOffersByResult(OffersFilterReq offersFilterReq);
    OffersDTO filter(String userId, OfferFilterReq offerFilterReq);

    OffersDTO.Offer createChat(long id);

    OffersDTO batchStatus(StatusUpdateReq statusUpdateReq);
}

