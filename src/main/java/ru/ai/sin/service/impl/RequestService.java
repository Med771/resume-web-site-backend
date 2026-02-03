package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;
import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.request.AddRequestReq;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.request.RequestUpdateStatusReq;
import ru.ai.sin.dto.request.UpdateRequestByChatReq;

public interface RequestService {
    RequestDTO getById(long id);

    PageResponse<RequestDTO> getByFilter(Pageable pageable, RequestFilterReq requestFilterReq);

    RequestDTO create(AddRequestReq addRequestReq);

    RequestDTO updateStatus(long id, RequestUpdateStatusReq requestUpdateStatusReq);

    RequestDTO updateByChatId(String chatId, UpdateRequestByChatReq updateRequestByChatReq);

    void deleteById(long id);
}
