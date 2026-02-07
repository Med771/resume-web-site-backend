package ru.ai.sin.tools;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.exception.models.NotFoundException;

import ru.ai.sin.logic.skill.SkillEnt;
import ru.ai.sin.logic.skill.SkillRepo;

import java.util.Collection;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SkillTools {

    private final SkillRepo skillRepo;

    @Transactional
    public SkillEnt getSkillOrThrow(long id) {
        return skillRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Failed to find skill with id " + id)
        );
    }

    @Transactional
    public Set<SkillEnt> getSkillsByIds(Collection<Long> ids) {
        return skillRepo.findAllByIdIn(ids);
    }
}
