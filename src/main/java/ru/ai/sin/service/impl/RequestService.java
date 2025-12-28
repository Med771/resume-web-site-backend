package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;
import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.request.AddRequestReq;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.request.RequestNewChatReq;
import ru.ai.sin.dto.request.RequestUpdateStatusReq;

public interface RequestService {
    RequestDTO getById(long id);

    PageResponse<RequestDTO> getByFilter(Pageable pageable, RequestFilterReq requestFilterReq);

    RequestDTO create(AddRequestReq addRequestReq);

    RequestDTO newChatById(long id, RequestNewChatReq requestNewChatReq);

    RequestDTO updateStatus(long id, RequestUpdateStatusReq requestUpdateStatusReq);
}
