package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.entity.SpecialityEnt;

import ru.ai.sin.exception.models.NotFoundException;

import ru.ai.sin.repository.SpecialityRepo;

@Component
@RequiredArgsConstructor
public class SpecialityTools {

    private final SpecialityRepo specialityRepo;

    @Transactional(readOnly = true)
    public SpecialityEnt getSpecialityOrThrow(long specialityId) {
        return specialityRepo.findById(specialityId).orElseThrow(
                () -> new NotFoundException("Failed to find special by id " + specialityId)
        );
    }
}
