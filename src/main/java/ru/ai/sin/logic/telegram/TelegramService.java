package ru.ai.sin.logic.telegram;

import ru.ai.sin.logic.request.dto.RequestDTO;
import ru.ai.sin.logic.telegram.dto.UpdateRequestTelegramReq;

public interface TelegramService {

    RequestDTO updateRequest(long id, UpdateRequestTelegramReq req);
}
