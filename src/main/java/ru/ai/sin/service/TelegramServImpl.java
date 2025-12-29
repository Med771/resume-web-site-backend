package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.telegram.RecruiterTelegramDTO;
import ru.ai.sin.dto.telegram.SetTelegramUserIdReq;
import ru.ai.sin.dto.telegram.StudentTelegramDTO;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.model.ContactInformation;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.repository.RecruiterRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.TelegramService;
import ru.ai.sin.service.tools.TelegramTools;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TelegramServImpl implements TelegramService {

    private final StudentRepo studentRepo;
    private final RecruiterRepo recruiterRepo;

    private final TelegramTools telegramTools;

    @Override
    @Transactional(readOnly = true)
    public StudentTelegramDTO getStudentTelegramByUsername(String telegramUsername) {
        StudentEnt studentEnt = studentRepo
                .findByContactInformationTelegramUsername(telegramUsername)
                .orElseThrow(() -> new NotFoundException("Failed to find student by telegram username: " + telegramUsername));

        return telegramTools.mapStudentToDTO(studentEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public RecruiterTelegramDTO getRecruiterTelegramByUsername(String telegramUsername) {
        RecruiterEnt recruiterEnt = recruiterRepo
                .findByContactInformationTelegramUsername(telegramUsername)
                .orElseThrow(() -> new NotFoundException("Failed to find recruiter by telegram username: " + telegramUsername));

        return telegramTools.mapRecruiterToDTO(recruiterEnt);
    }

    @Override
    @Transactional
    public StudentTelegramDTO setStudentTelegramUserId(UUID studentId, SetTelegramUserIdReq setTelegramUserIdReq) {
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

