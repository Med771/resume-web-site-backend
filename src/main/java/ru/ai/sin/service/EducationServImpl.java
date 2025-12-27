package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.education.*;

import ru.ai.sin.entity.EducationEnt;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.mapper.EducationMapper;
import ru.ai.sin.repository.EducationRepo;

import ru.ai.sin.service.impl.EducationService;
import ru.ai.sin.service.tools.EducationTools;


@Slf4j
@Service
@RequiredArgsConstructor
public class EducationServImpl implements EducationService {

    private final EducationRepo educationRepo;

    private final EducationMapper educationMapper;

    private final EducationTools educationTools;

    private final SecurityHelper securityHelper;

    @Override
    @Transactional(readOnly = true)
    public EducationDTO getById(long id) {
        return educationMapper.toDTO(educationTools.getEducationOrThrow(id));
    }

    @Override
    public PageResponse<EducationDTO> getAll(Pageable pageable) {
        Page<EducationEnt> educationPage = educationRepo.findAll(pageable);

        return new PageResponse<>(
                educationPage.getContent().stream().map(educationMapper::toDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                educationPage.getTotalElements(),
                educationPage.getTotalPages());
    }

    @Override
    public EducationDTO create(AddEducationReq addEducationReq) {
        EducationDTO educationDTO = educationMapper.toDTO(educationRepo.save(educationMapper.toEntity(addEducationReq)));

        log.info("User: {}, created a new education: {}", securityHelper.getCurrentUsername(), educationDTO);

        return educationDTO;
    }

    @Override
    @Transactional
    public EducationDTO update(
            long id,
            UpdateEducationReq updateEducationReq
    ) {
        EducationEnt educationEnt = educationTools.getEducationOrThrow(id);

        educationMapper.updateEntityFromDto(updateEducationReq, educationEnt);

        EducationDTO educationDTO = educationMapper.toDTO(educationEnt);

        log.info("User: {}, updated a education: {} with data: {}", securityHelper.getCurrentUsername(), id, educationDTO);

        return educationDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        EducationEnt educationEnt = educationTools.getEducationOrThrow(id);

        try {
            educationRepo.delete(educationEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting education: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting education");
        }

        log.info("User: {}, deleted a education: {} with data: {}", securityHelper.getCurrentUsername(), id, educationEnt);
    }
}
