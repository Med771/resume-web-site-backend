package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.entity.EducationEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.repository.EducationRepo;

@Component
@RequiredArgsConstructor
public class EducationTools {

    private final EducationRepo educationRepo;

    @Transactional(readOnly = true)
    public EducationEnt getEducationOrThrow(long id) {
        return educationRepo.findById(id).orElseThrow(
                () -> new NotFoundException("Failed to find education with id" + id)
        );
    }
}
