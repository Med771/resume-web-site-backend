package ru.ai.sin.logic.institution;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.logic.institution.dto.*;

import ru.ai.sin.tools.EducationTools;
import ru.ai.sin.tools.InstitutionTools;
import ru.ai.sin.tools.StudentTools;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepo institutionRepo;

    private final InstitutionMapper institutionMapper;

    private final InstitutionTools institutionTools;

    private final EducationTools educationTools;
    private final StudentTools studentTools;

    private final SecurityHelper securityHelper;

    private void updateActiveEducationOrThrow(long educationId, InstitutionEnt institutionEnt) {
        institutionEnt.setEducation(educationTools.getEducationOrThrow(educationId));
    }

    private void updateActiveStudentOrThrow(UUID studentId, InstitutionEnt institutionEnt) {
        institutionEnt.setStudent(studentTools.getStudentOrThrow(studentId));
    }

    @Override
    public InstitutionDTO getById(long id) {
        InstitutionEnt institutionEnt = institutionTools.getInstitutionOrThrow(id);

        return institutionTools.mapToDTO(institutionEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<InstitutionDTO> getAllByFilter(Pageable pageable, FilterInstitutionReq filterInstitutionReq) {
        Page<InstitutionEnt> page = institutionRepo.findAll(
                InstitutionSpecifications.byFilters(filterInstitutionReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(institutionTools::mapToDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    @Transactional
    public InstitutionDTO create(AddInstitutionReq addInstitutionReq) {
        InstitutionEnt institutionEnt = institutionMapper.toEntity(addInstitutionReq);

        updateActiveEducationOrThrow(addInstitutionReq.educationId(), institutionEnt);
        updateActiveStudentOrThrow(addInstitutionReq.studentId(), institutionEnt);

        try {
            institutionEnt = institutionRepo.save(institutionEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Institution already exists");

            throw new BadRequestException("Institution already exists");
        }

        InstitutionDTO institutionDTO = institutionTools.mapToDTO(institutionEnt);

        log.info("User: {}, created a new institution: {}", securityHelper.getCurrentUsername(), institutionDTO);

        return institutionDTO;
    }

    @Override
    @Transactional
    public InstitutionDTO update(
            long id,
            UpdateInstitutionReq updateInstitutionReq
    ) {
        InstitutionEnt institutionEnt = institutionTools.getInstitutionOrThrow(id);

        institutionMapper.updateEntityFromDto(updateInstitutionReq, institutionEnt);

        if (!Objects.equals(institutionEnt.getEducation().getId(), updateInstitutionReq.educationId())) {
            updateActiveEducationOrThrow(updateInstitutionReq.educationId(), institutionEnt);
        }
        if (!Objects.equals(institutionEnt.getStudent().getId(), updateInstitutionReq.studentId())) {
            updateActiveStudentOrThrow(updateInstitutionReq.studentId(), institutionEnt);
        }

        InstitutionDTO institutionDTO = institutionTools.mapToDTO(institutionEnt);

        log.info("User: {}, updated a institution: {} with data: {}", securityHelper.getCurrentUsername(), id, institutionDTO);

        return institutionDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        InstitutionEnt institutionEnt = institutionTools.getInstitutionOrThrow(id);

        try {
            institutionRepo.delete(institutionEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting institution: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting institution");
        }

        log.info("User: {}, deleted a institution: {} with data: {}", securityHelper.getCurrentUsername(), id, institutionEnt);
    }
}
