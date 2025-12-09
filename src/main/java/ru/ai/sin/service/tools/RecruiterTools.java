package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.repository.RecruiterRepo;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RecruiterTools {

    private final RecruiterRepo recruiterRepo;

    @Transactional(readOnly = true)
    public RecruiterEnt getRecruiterOrThrow(UUID recruiterId) {
        return recruiterRepo.findById(recruiterId).orElseThrow(
                () -> new NotFoundException("Failed to find recruiter by id " + recruiterId)
        );
    }
}
