package ru.ai.sin.logic.telegram;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.logic.request.RequestEnt;
import ru.ai.sin.logic.request.RequestRepo;
import ru.ai.sin.logic.request.dto.RequestDTO;
import ru.ai.sin.logic.telegram.dto.UpdateRequestTelegramReq;
import ru.ai.sin.tools.RequestTools;

@Service
@RequiredArgsConstructor
public class TelegramServiceImpl implements TelegramService {

    private final RequestRepo requestRepo;
    private final RequestTools requestTools;

    @Override
    @Transactional
    public RequestDTO updateRequest(long id, UpdateRequestTelegramReq req) {
        RequestEnt ent = requestTools.getRequestOrThrow(id);

        if (req.chatId() != null) {
            ent.setChatId(req.chatId());
        }
        if (req.result() != null) {
            ent.setResult(req.result());
        }
        if (Boolean.TRUE.equals(req.hasRecruiterMessage())) {
            ent.setHasRecruiterMessage(true);
        }
        if (Boolean.TRUE.equals(req.hasStudentMessage())) {
            ent.setHasStudentMessage(true);
        }
        if (req.studentResponseText() != null) {
            ent.setStudentResponseText(req.studentResponseText());
        }

        ent = requestRepo.save(ent);

        return requestTools.mapToDTO(ent);
    }
}
