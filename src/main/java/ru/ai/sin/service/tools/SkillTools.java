package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.repository.SkillRepo;

@Component
@RequiredArgsConstructor
public class SkillTools {

    private final SkillRepo skillRepo;

    @Transactional(readOnly = true)
    public SkillEnt getSkillOrThrow(long id) {
        return skillRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Failed to find skill with id " + id)
        );
    }
}
