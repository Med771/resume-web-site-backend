package ru.ai.sin.logic.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.logic.recruiter.RecruiterEnt;
import ru.ai.sin.logic.recruiter.dto.AddRecruiterReq;
import ru.ai.sin.logic.request.dto.*;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;

import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.tools.RecruiterTools;
import ru.ai.sin.tools.RequestTools;
import ru.ai.sin.tools.StudentTools;
import ru.ai.sin.tools.UserTools;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepo requestRepo;

    private final RequestTools requestTools;
    private final StudentTools studentTools;
    private final RecruiterTools recruiterTools;

    private final UserRepo userRepo;

    private final UserTools userTools;

    private final SecurityHelper securityHelper;

    @Override
    @Transactional(readOnly = true)
    public RequestDTO getById(long id) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        return requestTools.mapToDTO(requestEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<RequestDTO> getByFilter(Pageable pageable, FilterRequestReq filterRequestReq) {
        Page<RequestEnt> page = requestRepo
                .findAll(
                        RequestSpecifications.byFilters(filterRequestReq),
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
    public RequestDTO create(AddRequestReq addRequestReq) {
        RecruiterEnt recruiterEnt = resolveRecruiterForNewRequest(addRequestReq);

        StudentEnt studentEnt = studentTools.getStudentOrThrow(addRequestReq.studentId());

        RequestEnt requestEnt = new RequestEnt();
        requestEnt.setRecruiter(recruiterEnt);
        requestEnt.setStudent(studentEnt);

        try {
            requestEnt = requestRepo.save(requestEnt);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Error while creating request: {}", ex.getMessage());
            throw new BadRequestException("Error while creating request");
        }

        RequestDTO requestDTO = requestTools.mapToDTO(requestEnt);
        log.info("Created new request: {} for recruiter: {} and student: {}", requestEnt.getId(), recruiterEnt.getId(), studentEnt.getId());

        var contact = recruiterEnt.getContactInformation();
        if (contact != null && contact.getTelegramUserId() != null) {
            requestTools.updateAllStatusForRecruiter(recruiterEnt.getId());
        }

        return requestDTO;
    }

    /**
     * Рекрутер из привязки к аккаунту или создание/поиск по телу заявки с последующей привязкой к пользователю.
     */
    private RecruiterEnt resolveRecruiterForNewRequest(AddRequestReq addRequestReq) {
        Optional<UserEnt> currentUserOpt = userTools.findCurrentUserFetchingRecruiter();
        return currentUserOpt
                .map(UserEnt::getRecruiter)
                .orElseGet(() -> createRecruiterAndLinkForRequest(addRequestReq, currentUserOpt));
    }

    private RecruiterEnt createRecruiterAndLinkForRequest(
            AddRequestReq addRequestReq,
            Optional<UserEnt> currentUserOpt
    ) {
        validateRecruiterPayloadWhenUnlinked(addRequestReq);
        AddRecruiterReq addRecruiterReq = new AddRecruiterReq(
                addRequestReq.companyName(),
                addRequestReq.firstName(),
                addRequestReq.lastName(),
                addRequestReq.email(),
                addRequestReq.phoneNumber(),
                addRequestReq.telegramUsername()
        );
        RecruiterEnt recruiterEnt = recruiterTools.findOrCreateRecruiter(addRecruiterReq);
        currentUserOpt.ifPresent(u -> linkRecruiterToUserIfNeeded(u, recruiterEnt));
        return recruiterEnt;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);

        try {
            requestRepo.delete(requestEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting request: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting request");
        }

        log.info("User: {}, deleted a request: {} with data: {}", securityHelper.getCurrentUsername(), id, requestEnt);
    }

    private void validateRecruiterPayloadWhenUnlinked(AddRequestReq addRequestReq) {
        if (addRequestReq.companyName() == null || addRequestReq.companyName().isBlank()) {
            throw new BadRequestException(
                    "Укажите companyName, пока к аккаунту не привязан профиль рекрутера (GET /recruiter/me)");
        }
    }

    private void linkRecruiterToUserIfNeeded(UserEnt user, RecruiterEnt recruiterEnt) {
        if (user.getRecruiter() != null) {
            return;
        }
        var existingOwner = userRepo.findByRecruiter_Id(recruiterEnt.getId());
        if (existingOwner.isPresent() && !existingOwner.get().getId().equals(user.getId())) {
            throw new BadRequestException(
                    "Этот профиль рекрутера уже привязан к другому аккаунту");
        }
        user.setRecruiter(recruiterEnt);
        userRepo.save(user);
        log.info("Linked recruiter {} to user {}", recruiterEnt.getId(), user.getId());
    }
}
