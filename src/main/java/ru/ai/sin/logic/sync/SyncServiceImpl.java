package ru.ai.sin.logic.sync;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.recruiter.RecruiterEnt;
import ru.ai.sin.logic.recruiter.RecruiterRepo;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.sync.dto.SyncDTO;
import ru.ai.sin.logic.sync.dto.SyncUpdateReq;
import ru.ai.sin.models.embeddables.ContactInformation;
import ru.ai.sin.models.enums.SyncTypeEnum;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncServiceImpl implements SyncService {

    private final RecruiterRepo recruiterRepo;
    private final StudentRepo studentRepo;

    @Override
    @Transactional(readOnly = true)
    public SyncDTO getByUserId(String userId) {
        return recruiterRepo.findByContactInformationTelegramUserId(userId)
                .map(RecruiterEnt::getId)
                .map(id -> new SyncDTO(SyncTypeEnum.RE, id))
                .or(() -> studentRepo.findByContactInformationTelegramUserId(userId)
                        .map(StudentEnt::getId)
                        .map(id -> new SyncDTO(SyncTypeEnum.ST, id)))
                .orElseThrow(() -> new NotFoundException("No recruiter or student linked to user_id: " + userId));
    }

    @Override
    @Transactional
    public void update(UUID id, SyncUpdateReq req) {
        String newUserId = req.userId();

        if (req.type() == SyncTypeEnum.RE) {
            Optional<RecruiterEnt> ent = recruiterRepo.findById(id);

            if (ent.isPresent()) {
                if (ent.get().getContactInformation() == null) {
                    ent.get().setContactInformation(new ContactInformation());
                }

                ent.get().getContactInformation().setTelegramUserId(newUserId);

                return;
            }
        }
        else if (req.type() == SyncTypeEnum.ST) {
            Optional<StudentEnt> ent = studentRepo.findById(id);

            if (ent.isPresent()) {
                if (ent.get().getContactInformation() == null) {
                    ent.get().setContactInformation(new ContactInformation());
                }

                ent.get().getContactInformation().setTelegramUserId(newUserId);

                return;
            }
        }

        throw new NotFoundException("No recruiter or student with id: " + id);
    }
}
