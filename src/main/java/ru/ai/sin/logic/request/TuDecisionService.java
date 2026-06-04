package ru.ai.sin.logic.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.helper.AccountAccessHelper;
import ru.ai.sin.logic.chat.ChatEnt;
import ru.ai.sin.logic.chat.ChatService;
import ru.ai.sin.logic.chat.ChatSystemEvent;
import ru.ai.sin.logic.request.dto.TuDecisionReq;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.models.enums.RejectionReasonCode;
import ru.ai.sin.models.enums.ResultEnum;
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.tools.RequestTools;
import ru.ai.sin.tools.UserTools;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TuDecisionService {

    private final RequestRepo requestRepo;
    private final RequestTools requestTools;
    private final UserTools userTools;
    private final ChatService chatService;
    private final AccountAccessHelper accountAccessHelper;

    @Transactional
    public void decideOnRequest(long requestId, TuDecisionReq req) {
        accountAccessHelper.requireApprovedAccount();
        RequestEnt r = requestTools.getRequestOrThrow(requestId);
        UserEnt user = userTools.findCurrentUserFetchingLinks()
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (Boolean.FALSE.equals(req.accept())) {
            applyRejection(r, req);
            return;
        }
        if (req.accept() == null || !req.accept()) {
            throw new BadRequestException("Укажите accept: true или false");
        }

        if (user.getRole() == RoleEnum.STUDENT) {
            confirmAsStudent(r, user);
        } else if (user.getRole() == RoleEnum.RECRUITER) {
            confirmAsRecruiter(r, user);
        } else if (user.getRole() == RoleEnum.ADMIN) {
            throw new BadRequestException("Админ не участвует в ТУ");
        } else {
            throw new BadRequestException("Недоступно для вашей роли");
        }
    }

    private void confirmAsStudent(RequestEnt r, UserEnt user) {
        if (user.getStudent() == null || !user.getStudent().getId().equals(r.getStudent().getId())) {
            throw new BadRequestException("Это не ваша заявка");
        }
        if (r.getResult() != ResultEnum.STUDENT_CONFIRMED && r.getResult() != ResultEnum.WAITING) {
            throw new BadRequestException("Неверный этап заявки для ТУ");
        }
        r.setStudentTuConfirmedAt(LocalDateTime.now());
        if (r.getRecruiterTuConfirmedAt() != null) {
            r.setResult(ResultEnum.SUCCESS);
            postSuccess(r);
        } else {
            r.setResult(ResultEnum.STUDENT_CONFIRMED);
        }
        requestRepo.save(r);
    }

    private void confirmAsRecruiter(RequestEnt r, UserEnt user) {
        if (user.getRecruiter() == null || !user.getRecruiter().getId().equals(r.getRecruiter().getId())) {
            throw new BadRequestException("Это не ваша заявка");
        }
        r.setRecruiterTuConfirmedAt(LocalDateTime.now());
        if (r.getStudentTuConfirmedAt() != null) {
            r.setResult(ResultEnum.SUCCESS);
            postSuccess(r);
        } else {
            r.setResult(ResultEnum.RECRUITER_CONFIRMED);
        }
        requestRepo.save(r);
    }

    private void applyRejection(RequestEnt r, TuDecisionReq req) {
        AccountAccessHelper.validateRejection(req.reasonCode(), req.comment());
        r.setRejectionReasonCode(RejectionReasonCode.fromCode(req.reasonCode()).getCode());
        r.setRejectionComment(req.comment());
        r.setResult(ResultEnum.REFUSAL);
        requestRepo.save(r);
        ChatEnt chat = r.getAppChat();
        chatService.postSystemMessage(chat, ChatSystemEvent.TU_REJECTED,
                "Отказ по заявке №" + r.getId() + ": " + req.reasonCode());
    }

    private void postSuccess(RequestEnt r) {
        ChatEnt chat = r.getAppChat();
        chatService.postSystemMessage(chat, ChatSystemEvent.TU_CONFIRMED,
                "Обе стороны подтвердили ТУ по заявке №" + r.getId() + ". Успех.");
    }
}
