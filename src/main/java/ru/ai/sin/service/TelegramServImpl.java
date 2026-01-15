package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.recruiter.RecruiterRes;
import ru.ai.sin.dto.request.RequestFilterReq;
import ru.ai.sin.dto.student.StudentRes;
import ru.ai.sin.dto.telegram.*;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.model.ContactInformation;
import ru.ai.sin.entity.spec.RequestSpecifications;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.repository.RecruiterRepo;
import ru.ai.sin.repository.RequestRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.TelegramService;
import ru.ai.sin.service.tools.TelegramTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramServImpl implements TelegramService {

    private final StudentRepo studentRepo;
    private final RecruiterRepo recruiterRepo;
    private final RequestRepo requestRepo;

    private final TelegramTools telegramTools;

    @Override
    public StudentTelegramDTO getStudentByTelegramUserId(String userId) {
        Optional<StudentEnt> studentEnt = studentRepo.findByContactInformationTelegramUserId(userId);

        if (studentEnt.isPresent()) {
            return telegramTools.mapStudentToDTO(studentEnt.get());
        }

        throw new NotFoundException("Student not found");
    }

    @Override
    public RecruiterTelegramDTO getRecruiterByTelegramUserId(String userId) {
        Optional<RecruiterEnt> recruiterEnt = recruiterRepo.findByContactInformationTelegramUserId(userId);

        if (recruiterEnt.isPresent()) {
            return telegramTools.mapRecruiterToDTO(recruiterEnt.get());
        }

        throw new NotFoundException("Recruiter not found");
    }

    @Override
    @Transactional
    public OffersDTO filter(String userId, OfferFilterReq offerFilterReq) {
        List<RequestEnt> requests;
        
        if (offerFilterReq.isStud()) {
            Optional<StudentEnt> studentEnt = studentRepo.findByContactInformationTelegramUserId(userId);

            if (studentEnt.isEmpty()) {
                throw new NotFoundException("Student not found");
            }
            
            requests = requestRepo.findAll(RequestSpecifications.byFilters(
                    new RequestFilterReq(offerFilterReq.results(), null, studentEnt.get().getId())));
        }
        else {
            Optional<RecruiterEnt> recruiterEnt = recruiterRepo.findByContactInformationTelegramUserId(userId);
            
            if (recruiterEnt.isEmpty()) {
                throw new NotFoundException("Recruiter not found");
            }

            requests = requestRepo.findAll(RequestSpecifications.byFilters(
                    new RequestFilterReq(offerFilterReq.results(), recruiterEnt.get().getId(), null)));
        }

        List<OffersDTO.Offer> offers = getOffers(requests);

        return new OffersDTO(offers);
    }

    private static List<OffersDTO.Offer> getOffers(List<RequestEnt> requests) {
        List<OffersDTO.Offer> offers = new ArrayList<>();

        for (RequestEnt requestEnt : requests) {
            StudentRes studentRes = new StudentRes(
                    requestEnt.getStudent().getId(),
                    requestEnt.getStudent().getSpeciality().getName(),
                    requestEnt.getStudent().getUserInformation().getFirstName() +
                            " " +
                            requestEnt.getStudent().getUserInformation().getLastName()
            );

            RecruiterRes recruiterRes = new RecruiterRes(
                    requestEnt.getRecruiter().getId(),
                    requestEnt.getRecruiter().getCompanyName(),
                    requestEnt.getRecruiter().getUserInformation().getFirstName() +
                            " " +
                            requestEnt.getRecruiter().getUserInformation().getLastName()
            );

            offers.add(new OffersDTO.Offer(
                    requestEnt.getId(),
                    requestEnt.getChatId(),
                    requestEnt.getResult(),
                    requestEnt.getChatUrl(),
                    studentRes,
                    recruiterRes
            ));
        }
        return offers;
    }

    @Override
    @Transactional
    public StudentTelegramDTO setStudentTelegramUserId(UUID studentId, SetTelegramUserIdReq setTelegramUserIdReq) {
        if (recruiterRepo.findByContactInformationTelegramUserId(setTelegramUserIdReq.telegramUserId()).isPresent()) {
            throw new BadRequestException("Telegram user id already set for recruiter when student: " + studentId);
        }

        StudentEnt studentEnt = telegramTools.getStudentOrThrow(studentId);

        if (studentEnt.getContactInformation() == null) {
            studentEnt.setContactInformation(new ContactInformation());
        }

        if (studentEnt.getContactInformation().getTelegramUserId() != null) {
            throw new BadRequestException("Telegram user id already set for student: " + studentId);
        }

        studentEnt.getContactInformation().setTelegramUserId(setTelegramUserIdReq.telegramUserId());
        studentEnt = studentRepo.save(studentEnt);

        StudentTelegramDTO studentTelegramDTO = telegramTools.mapStudentToDTO(studentEnt);

        log.info("Set telegram user id for student: {} with telegramUserId: {}", studentId, setTelegramUserIdReq.telegramUserId());

        return studentTelegramDTO;
    }

    @Override
    @Transactional
    public RecruiterTelegramDTO setRecruiterTelegramUserId(UUID recruiterId, SetTelegramUserIdReq setTelegramUserIdReq) {
        if (studentRepo.findByContactInformationTelegramUserId(setTelegramUserIdReq.telegramUserId()).isPresent()) {
            throw new BadRequestException("Telegram user id already set for student when recruiter: " + recruiterId);
        }

        RecruiterEnt recruiterEnt = telegramTools.getRecruiterOrThrow(recruiterId);

        if (recruiterEnt.getContactInformation().getTelegramUserId() != null) {
            throw new BadRequestException("Telegram user id already set for recruiter: " + recruiterId);
        }

        recruiterEnt.getContactInformation().setTelegramUserId(setTelegramUserIdReq.telegramUserId());
        recruiterEnt = recruiterRepo.save(recruiterEnt);

        RecruiterTelegramDTO recruiterTelegramDTO = telegramTools.mapRecruiterToDTO(recruiterEnt);

        log.info("Set telegram user id for recruiter: {} with telegramUserId: {}", recruiterId, setTelegramUserIdReq.telegramUserId());

        return recruiterTelegramDTO;
    }

    @Override
    @Transactional
    public StudentTelegramDTO clearStudentTelegramUserId(UUID studentId) {
        StudentEnt studentEnt = telegramTools.getStudentOrThrow(studentId);

        if (studentEnt.getContactInformation() == null) {
            studentEnt.setContactInformation(new ContactInformation());
        }
        else {
            studentEnt.getContactInformation().setTelegramUserId(null);
        }

        StudentTelegramDTO studentTelegramDTO = telegramTools.mapStudentToDTO(studentEnt);

        log.info("Cleared telegram user id for student: {}", studentId);

        return studentTelegramDTO;
    }

    @Override
    @Transactional
    public RecruiterTelegramDTO clearRecruiterTelegramUserId(UUID recruiterId) {
        RecruiterEnt recruiterEnt = telegramTools.getRecruiterOrThrow(recruiterId);

        recruiterEnt.getContactInformation().setTelegramUserId(null);
        recruiterEnt = recruiterRepo.save(recruiterEnt);

        RecruiterTelegramDTO recruiterTelegramDTO = telegramTools.mapRecruiterToDTO(recruiterEnt);

        log.info("Cleared telegram user id for recruiter: {}", recruiterId);

        return recruiterTelegramDTO;
    }
}

