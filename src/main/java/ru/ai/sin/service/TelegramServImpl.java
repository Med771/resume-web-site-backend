package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.telegram.*;
import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.spec.RequestSpecifications;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.helper.FastHelper;
import ru.ai.sin.repository.RecruiterRepo;
import ru.ai.sin.repository.RequestRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.TelegramService;
import ru.ai.sin.service.tools.RequestTools;
import ru.ai.sin.service.tools.TelegramTools;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramServImpl implements TelegramService {

    private final StudentRepo studentRepo;
    private final RecruiterRepo recruiterRepo;
    private final RequestRepo requestRepo;

    private final TelegramTools telegramTools;
    private final RequestTools requestTools;

    private final FastHelper fastHelper;

    @Override
    public TelegramUserRes getByTelegramUserId(String telegramUserId) {
        var studentOpt = studentRepo.findByContactInformationTelegramUserId(telegramUserId);
        if (studentOpt.isPresent()) {
            return TelegramUserRes.student(telegramTools.mapStudentToDTO(studentOpt.get()));
        }
        var recruiterOpt = recruiterRepo.findByContactInformationTelegramUserId(telegramUserId);
        if (recruiterOpt.isPresent()) {
            return TelegramUserRes.recruiter(telegramTools.mapRecruiterToDTO(recruiterOpt.get()));
        }
        throw new NotFoundException("User not found by telegram user id: " + telegramUserId);
    }

    @Override
    public OffersDTO.Offer getById(long id) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);
        return telegramTools.mapRequestToOffer(requestEnt);
    }

    @Override
    public OffersDTO getAllOffersByResult(OffersFilterReq offersFilterReq) {
        return new OffersDTO(getOffers(requestRepo.findAllByResultIn(offersFilterReq.results())));
    }

    @Override
    @Transactional
    public OffersDTO filter(String userId, OfferFilterReq offerFilterReq) {
        UUID studentId = null;
        UUID recruiterId = null;
        if (offerFilterReq.isStud()) {
            studentId = studentRepo.findByContactInformationTelegramUserId(userId)
                    .orElseThrow(() -> new NotFoundException("Student not found"))
                    .getId();
        } else {
            recruiterId = recruiterRepo.findByContactInformationTelegramUserId(userId)
                    .orElseThrow(() -> new NotFoundException("Recruiter not found"))
                    .getId();
        }
        List<RequestEnt> requests = requestRepo.findAll(
                RequestSpecifications.byFilters(new RequestFilterReq(offerFilterReq.results(), recruiterId, studentId)));
        return new OffersDTO(getOffers(requests));
    }

    private List<OffersDTO.Offer> getOffers(List<RequestEnt> requests) {
        return requests.stream()
                .map(req -> {
                    try {
                        return Optional.of(telegramTools.mapRequestToOffer(req));
                    } catch (NullPointerException ex) {
                        log.warn("NPE in get offers. Request Entity: {}", req.getId());
                        return Optional.<OffersDTO.Offer>empty();
                    }
                })
                .flatMap(Optional::stream)
                .toList();
    }

    @Override
    @Transactional
    public OffersDTO.Offer createChat(long id) {
        RequestEnt requestEnt = requestTools.getRequestOrThrow(id);
        try {
            var resp = fastHelper.createChat("Чат с кандидатом: %s %s и компанией: %s".formatted(
                    requestEnt.getStudent().getUserInformation().getLastName(),
                    requestEnt.getStudent().getUserInformation().getFirstName(),
                    requestEnt.getRecruiter().getCompanyName()));
            requestEnt.setChatId(String.valueOf(resp.chatId()));
            requestEnt.setChatUrl(resp.inviteLink());
        } catch (Exception e) {
            log.warn("Error while creating chat: {}", e.getMessage());
        }
        return telegramTools.mapRequestToOffer(requestEnt);
    }

    @Override
    @Transactional
    public OffersDTO batchStatus(StatusUpdateReq statusUpdateReq) {
        var ids = statusUpdateReq.newStatuses().stream().map(StatusUpdateReq.Pair::id).toList();
        List<RequestEnt> requests = requestRepo.findAllByIdIn(ids);
        Map<Long, RequestEnt> requestMap = requests.stream().collect(Collectors.toMap(RequestEnt::getId, Function.identity()));
        statusUpdateReq.newStatuses().forEach(pair -> {
            if (requestMap.containsKey(pair.id())) {
                requestMap.get(pair.id()).setResult(pair.result());
            }
        });
        requestRepo.saveAll(requests);
        return new OffersDTO(getOffers(requests));
    }
}

