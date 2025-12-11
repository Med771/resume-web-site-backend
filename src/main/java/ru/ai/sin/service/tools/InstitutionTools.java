package ru.ai.sin.service.tools;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.institution.InstitutionDTO;
import ru.ai.sin.dto.institution.InstitutionRes;
import ru.ai.sin.entity.InstitutionEnt;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.InstitutionMapper;
import ru.ai.sin.repository.InstitutionRepo;

@Component
@RequiredArgsConstructor
public class InstitutionTools {

    private final InstitutionRepo institutionRepo;

    private final InstitutionMapper institutionMapper;

    @Transactional(readOnly = true)
    public InstitutionEnt getInstitutionOrThrow(long id) {
        InstitutionEnt institutionEnt = institutionRepo.findWithEducationAndStudentById(id);

        if (institutionEnt == null) {
            throw new NotFoundException("Failed to find institution with id: " + id);
        }

        return institutionEnt;
    }

    public InstitutionDTO mapToDTO(InstitutionEnt institutionEnt) {
        InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

        return institutionMapper.toDTO(institutionEnt, institutionRes);
    }
}
