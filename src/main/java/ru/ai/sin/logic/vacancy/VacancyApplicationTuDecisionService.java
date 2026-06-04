package ru.ai.sin.logic.vacancy;

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
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.models.enums.VacancyApplicationStatus;
import ru.ai.sin.tools.UserTools;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VacancyApplicationTuDecisionService {

    private final VacancyApplicationRepo vacancyApplicationRepo;
    private final UserTools userTools;
    private final ChatService chatService;
    private final AccountAccessHelper accountAccessHelper;

    @Transactional
    public void decide(UUID applicationId, TuDecisionReq req) {
        accountAccessHelper.requireApprovedAccount();
        VacancyApplicationEnt app = vacancyApplicationRepo.findWithDetailsById(applicationId)
                .orElseThrow(() -> new NotFoundException("Отклик не найден: " + applicationId));
        if (app.getStatus() != VacancyApplicationStatus.ACCEPTED) {
            throw new BadRequestException("ТУ доступно только после принятия отклика");
        }
        UserEnt user = userTools.findCurrentUserFetchingLinks()
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (Boolean.FALSE.equals(req.accept())) {
            applyRejection(app, req);
            return;
        }
        if (req.accept() == null || !req.accept()) {
            throw new BadRequestException("Укажите accept: true или false");
        }

        if (user.getRole() == RoleEnum.STUDENT) {
            confirmAsStudent(app, user);
        } else if (user.getRole() == RoleEnum.RECRUITER) {
            confirmAsRecruiter(app, user);
        } else {
            throw new BadRequestException("Недоступно для вашей роли");
        }
    }

    private void confirmAsStudent(VacancyApplicationEnt app, UserEnt user) {
        if (user.getStudent() == null || !user.getStudent().getId().equals(app.getStudent().getId())) {
            throw new BadRequestException("Это не ваш отклик");
        }
        app.setStudentTuConfirmedAt(LocalDateTime.now());
        if (app.getRecruiterTuConfirmedAt() != null) {
            postSuccess(app);
        }
        vacancyApplicationRepo.save(app);
    }

    private void confirmAsRecruiter(VacancyApplicationEnt app, UserEnt user) {
        if (user.getRecruiter() == null
                || !user.getRecruiter().getId().equals(app.getVacancy().getRecruiter().getId())) {
            throw new BadRequestException("Это не ваша вакансия");
        }
        app.setRecruiterTuConfirmedAt(LocalDateTime.now());
        if (app.getStudentTuConfirmedAt() != null) {
            postSuccess(app);
        }
        vacancyApplicationRepo.save(app);
    }

    private void applyRejection(VacancyApplicationEnt app, TuDecisionReq req) {
        AccountAccessHelper.validateRejection(req.reasonCode(), req.comment());
        app.setRejectionReasonCode(RejectionReasonCode.fromCode(req.reasonCode()).getCode());
        app.setRejectionComment(req.comment());
        app.setStatus(VacancyApplicationStatus.REJECTED);
        vacancyApplicationRepo.save(app);
        ChatEnt chat = app.getAppChat();
        if (chat != null) {
            chatService.postSystemMessage(chat, ChatSystemEvent.TU_REJECTED,
                    "Отказ по отклику: " + req.reasonCode());
        }
    }

    private void postSuccess(VacancyApplicationEnt app) {
        ChatEnt chat = app.getAppChat();
        if (chat != null) {
            chatService.postSystemMessage(chat, ChatSystemEvent.TU_CONFIRMED,
                    "Обе стороны подтвердили ТУ по отклику на «" + app.getVacancy().getTitle() + "».");
        }
    }
}
