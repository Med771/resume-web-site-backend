package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.RecruiterMapper;
import ru.ai.sin.repository.RecruiterRepo;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RecruiterTools {

    private final RecruiterRepo recruiterRepo;

    private final RecruiterMapper recruiterMapper;

    @Transactional(readOnly = true)
    public RecruiterEnt getRecruiterOrThrow(UUID recruiterId) {
        return recruiterRepo.findById(recruiterId).orElseThrow(
                () -> new NotFoundException("Failed to find recruiter by id " + recruiterId)
        );
    }

    @Transactional
    public RecruiterEnt findOrCreateRecruiter(AddRecruiterReq addRecruiterReq) {
        RecruiterEnt recruiterEnt = recruiterRepo
                .findByUserInformationEmail(addRecruiterReq.email())
                .orElse(null);

        // Если рекрутер не найден, создаем нового
        if (recruiterEnt == null) {
            recruiterEnt = recruiterMapper.toEntity(addRecruiterReq);

            try {
                recruiterEnt = recruiterRepo.save(recruiterEnt);
                log.info("Created new recruiter: {} with email: {}", recruiterEnt.getId(), addRecruiterReq.email());
            } catch (DataIntegrityViolationException ex) {
                log.warn("Recruiter already exists: {}, {}", addRecruiterReq.email(), addRecruiterReq.telegramUsername());
                throw new BadRequestException("Recruiter already exists: %s, %s"
                        .formatted(addRecruiterReq.email(), addRecruiterReq.telegramUsername()));
            }
        }

        return recruiterEnt;
    }
}
