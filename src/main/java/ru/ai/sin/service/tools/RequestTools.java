package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.model.ResultEnum;
import ru.ai.sin.entity.spec.RequestSpecifications;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.RequestMapper;
import ru.ai.sin.repository.RequestRepo;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RequestTools {

    private final RequestRepo requestRepo;

    private final RequestMapper requestMapper;

    @Transactional(readOnly = true)
    public RequestEnt getRequestOrThrow(long id) {
        RequestEnt requestEnt = requestRepo.findById(id);
        if (requestEnt == null) {
            throw new NotFoundException("Failed to find request with id " + id);
        }
        return requestEnt;
    }

    @Transactional(readOnly = true)
    public RequestEnt getRequestByChatIdOrThrow(String chatId) {
        return requestRepo.findFirstByChatId(chatId)
                .orElseThrow(() -> new NotFoundException("Failed to find request with chatId " + chatId));
    }

    public RequestDTO mapToDTO(RequestEnt requestEnt) {
        return requestMapper.toDTO(requestEnt);
    }

    @Transactional
    public void updateAllStatusForRecruiter(UUID recruiterId) {
        List<RequestEnt> requests = requestRepo.findAll(
                RequestSpecifications.byFilters(
                        new RequestFilterReq(List.of(ResultEnum.CREATION), recruiterId, null))
        );
        requests.forEach(requestEnt -> requestEnt.setResult(ResultEnum.SYNC));
        requestRepo.saveAll(requests);
    }
}


