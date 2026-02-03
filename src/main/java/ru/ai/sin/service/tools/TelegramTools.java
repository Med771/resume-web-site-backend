package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.telegram.OffersDTO;
import ru.ai.sin.dto.telegram.RecruiterTelegramDTO;
import ru.ai.sin.dto.telegram.StudentTelegramDTO;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.entity.model.ContactInformation;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.TelegramMapper;
import ru.ai.sin.repository.RecruiterRepo;
import ru.ai.sin.repository.StudentRepo;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TelegramTools {

    private final StudentTools studentTools;
    private final RecruiterTools recruiterTools;
    private final StudentRepo studentRepo;
    private final RecruiterRepo recruiterRepo;
    private final TelegramMapper telegramMapper;

    @Transactional(readOnly = true)
    public StudentEnt getStudentOrThrow(UUID studentId) {
        return studentTools.getStudentOrThrow(studentId);
    }

    @Transactional(readOnly = true)
    public RecruiterEnt getRecruiterOrThrow(UUID recruiterId) {
        return recruiterTools.getRecruiterOrThrow(recruiterId);
    }

    public StudentTelegramDTO mapStudentToDTO(StudentEnt studentEnt) {
        return telegramMapper.studentToDTO(studentEnt);
    }

    public RecruiterTelegramDTO mapRecruiterToDTO(RecruiterEnt recruiterEnt) {
        return telegramMapper.recruiterToDTO(recruiterEnt);
    }

    public OffersDTO.Offer mapRequestToOffer(RequestEnt requestEnt) {
        return telegramMapper.toOffer(requestEnt);
    }

    public void ensureStudentContactInformation(StudentEnt student) {
        if (student.getContactInformation() == null) {
            student.setContactInformation(new ContactInformation());
        }
    }

    public void throwIfTelegramUserIdUsedByOther(String telegramUserId, boolean settingForStudent, UUID entityId) {
        if (settingForStudent && recruiterRepo.findByContactInformationTelegramUserId(telegramUserId).isPresent()) {
            throw new BadRequestException("Telegram user id already set for recruiter when student: " + entityId);
        }
        if (!settingForStudent && studentRepo.findByContactInformationTelegramUserId(telegramUserId).isPresent()) {
            throw new BadRequestException("Telegram user id already set for student when recruiter: " + entityId);
        }
    }
}

