package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.request.RequestNewChatReq;
import ru.ai.sin.dto.request.RequestUpdateStatusReq;
import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.spec.RequestSpecifications;
import ru.ai.sin.repository.RequestRepo;
import ru.ai.sin.service.impl.RequestService;
import ru.ai.sin.service.tools.RequestTools;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServImpl implements RequestService {

    private final RequestRepo requestRepo;

    private final RequestTools requestTools;

    @Override
    @Transactional(readOnly = true)
    public RequestDTO getById(long id) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        return requestTools.mapToDTO(requestEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RequestDTO> getByFilter(Pageable pageable, RequestFilterReq requestFilterReq) {
        Page<RequestEnt> page = requestRepo
                .findAll(
                        RequestSpecifications.byFilters(requestFilterReq),
                        pageable
                );

        return new PageResponse<>(
                page.getContent().stream().map(requestTools::mapToDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    @Transactional
    public RequestDTO newChatById(long id, RequestNewChatReq requestNewChatReq) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        requestEnt.setChatId(requestNewChatReq.chatId());
        requestEnt.setChatUrl(requestNewChatReq.chatUrl());

        requestEnt = requestRepo.save(requestEnt);

        RequestDTO requestDTO = requestTools.mapToDTO(requestEnt);

        log.info("Updated request chat: {} with chatId: {} and chatUrl: {}", id, requestNewChatReq.chatId(), requestNewChatReq.chatUrl());

        return requestDTO;
    }

    @Override
    @Transactional
    public RequestDTO updateStatus(long id, RequestUpdateStatusReq requestUpdateStatusReq) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        requestEnt.setResult(requestUpdateStatusReq.resultEnum());

        requestEnt = requestRepo.save(requestEnt);

        RequestDTO requestDTO = requestTools.mapToDTO(requestEnt);

        log.info("Updated request status: {} with status: {}", id, requestUpdateStatusReq.resultEnum());

        return requestDTO;
    }
}
