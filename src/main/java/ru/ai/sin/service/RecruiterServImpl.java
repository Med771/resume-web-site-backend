package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.recruiter.*;

import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.entity.spec.RecruiterSpecifications;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.mapper.RecruiterMapper;

import ru.ai.sin.repository.RecruiterRepo;

import ru.ai.sin.service.impl.RecruiterService;

import ru.ai.sin.service.tools.RecruiterTools;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterServImpl implements RecruiterService {

    private final RecruiterRepo recruiterRepo;

    private final RecruiterMapper recruiterMapper;

    private final RecruiterTools recruiterTools;

    private final SecurityHelper securityHelper;

    @Override
    public RecruiterDTO getById(UUID id) {
        return recruiterMapper.toDTO(recruiterTools.getRecruiterOrThrow(id));
    }

    @Override
    public PageResponse<RecruiterDTO> getAllByFilter(
            Pageable pageable,
            RecruiterFilterReq recruiterFilterReq
    ) {
        Page<RecruiterEnt> page = recruiterRepo.findAll(
                RecruiterSpecifications.byFilters(recruiterFilterReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(recruiterMapper::toDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    @Transactional
    public RecruiterDTO update(
            UUID id,
            UpdateRecruiterReq updateRecruiterReq
    ) {
        RecruiterEnt recruiterEnt = recruiterTools.getRecruiterOrThrow(id);

        recruiterMapper.updateEntityFromDto(updateRecruiterReq, recruiterEnt);

        RecruiterDTO recruiterDTO = recruiterMapper.toDTO(recruiterEnt);

        log.info("User: {}, updated a recruiter: {} with data: {}", securityHelper.getCurrentUsername(), id, recruiterDTO);

        return recruiterDTO;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        RecruiterEnt recruiterEnt = recruiterTools.getRecruiterOrThrow(id);

        try {
            recruiterRepo.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting recruiter: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting recruiter");
        }

        log.info("User: {}, deleted a recruiter: {} with data: {}", securityHelper.getCurrentUsername(), id, recruiterEnt);
    }
}
