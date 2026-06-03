package ru.ai.sin.logic.experience;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.models.PageResponse;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.logic.experience.dto.*;

import ru.ai.sin.tools.CompanyTools;
import ru.ai.sin.tools.ExperienceTools;

import ru.ai.sin.tools.StudentTools;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {

    private final ExperienceRepo experienceRepo;

    private final ExperienceMapper experienceMapper;

    private final ExperienceTools experienceTools;

    private final CompanyTools companyTools;
    private final StudentTools studentTools;

    private final SecurityHelper securityHelper;

    private void updateActiveCompanyOrThrow(long companyId, ExperienceEnt experienceEnt) {
        experienceEnt.setCompany(companyTools.getCompanyOrThrow(companyId));
    }

    private void updateActiveStudentOrThrow(UUID studentId, ExperienceEnt experienceEnt) {
        experienceEnt.setStudent(studentTools.getStudentOrThrow(studentId));
    }

    @Override
    @Transactional(readOnly = true)
    public ExperienceDTO getById(long id) {
        return experienceTools.mapToDTO(experienceTools.getExperienceOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<ExperienceDTO> getAllByFilter(Pageable pageable, FilterExperienceReq filterExperienceReq) {
        Page<ExperienceEnt> page = experienceRepo.findAll(
                ExperienceSpecifications.byFilters(filterExperienceReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(experienceTools::mapToDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    @Transactional
    public ExperienceDTO create(AddExperienceReq addExperienceReq) {
        ExperienceEnt experienceEnt = experienceMapper.toEntity(addExperienceReq);

        updateActiveCompanyOrThrow(addExperienceReq.companyId(), experienceEnt);
        updateActiveStudentOrThrow(addExperienceReq.studentId(), experienceEnt);

        try {
            experienceEnt = experienceRepo.save(experienceEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Experience already exists");

            throw new BadRequestException("Experience already exists");
        }

        ExperienceDTO experienceDTO = experienceTools.mapToDTO(experienceEnt);

        log.info("User: {}, created a new experience: {}", securityHelper.getCurrentUsername(), experienceDTO);

        return experienceDTO;
    }

    @Override
    @Transactional
    public ExperienceDTO update(
            long id,
            UpdateExperienceReq updateExperienceReq
    ) {
        ExperienceEnt experienceEnt = experienceTools.getExperienceOrThrow(id);

        experienceMapper.updateEntityFromDto(updateExperienceReq, experienceEnt);

        if (!Objects.equals(experienceEnt.getCompany().getId(), updateExperienceReq.companyId())) {
            updateActiveCompanyOrThrow(updateExperienceReq.companyId(), experienceEnt);
        }

        if (!Objects.equals(experienceEnt.getStudent().getId(), updateExperienceReq.studentId())) {
            updateActiveStudentOrThrow(updateExperienceReq.studentId(), experienceEnt);
        }

        ExperienceDTO experienceDTO = experienceTools.mapToDTO(experienceEnt);

        log.info("User: {}, updated a experience: {} with data: {}", securityHelper.getCurrentUsername(), id, experienceDTO);

        return experienceDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        ExperienceEnt experienceEnt = experienceTools.getExperienceOrThrow(id);

        try {
            experienceRepo.delete(experienceEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting experience: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting experience");
        }

        log.info("User: {}, deleted a experience: {} with data: {}", securityHelper.getCurrentUsername(), id, experienceEnt);
    }
}
