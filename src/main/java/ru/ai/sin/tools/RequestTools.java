package ru.ai.sin.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.logic.request.dto.RequestDTO;
import ru.ai.sin.logic.request.RequestEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.request.RequestMapper;
import ru.ai.sin.logic.request.RequestRepo;

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

    public RequestDTO mapToDTO(RequestEnt requestEnt) {
        return requestMapper.toDTO(requestEnt);
    }
}
