package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;

import ru.ai.sin.dto.speciality.*;

import ru.ai.sin.entity.SpecialityEnt;
import ru.ai.sin.entity.spec.SpecialitySpecifications;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.mapper.SpecialityMapper;

import ru.ai.sin.repository.SpecialityRepo;

import ru.ai.sin.service.impl.SpecialityService;
import ru.ai.sin.service.tools.SpecialityTools;


@Slf4j
@Service
@RequiredArgsConstructor
public class SpecialityServImpl implements SpecialityService {

    private final SpecialityRepo specialityRepo;

    private final SpecialityMapper specialityMapper;

    private final SpecialityTools specialityTools;

    private final SecurityHelper securityHelper;

    @Override
    public SpecialityDTO getById(long id) {
        return specialityMapper.toDTO(specialityTools.getSpecialityOrThrow(id));
    }

    @Override
    public PageResponse<SpecialityDTO> getAllByFilter(Pageable pageable, SpecialityFilterReq specialityFilterReq) {
        Page<SpecialityEnt> page = specialityRepo.findAll(
                SpecialitySpecifications.byFilters(specialityFilterReq),
                pageable
        );

        return new PageResponse<>(
                page.getContent().stream().map(specialityMapper::toDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }


    @Override
    @Transactional
    public SpecialityDTO create(AddSpecialityReq addSpecialityReq) {
        SpecialityEnt specialityEnt = specialityMapper.toEntity(addSpecialityReq);

        try {
            specialityEnt = specialityRepo.save(specialityEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Speciality already exists: {}", addSpecialityReq.name());

            throw new BadRequestException("Speciality already exists: " + addSpecialityReq.name());
        }

        SpecialityDTO specialityDTO = specialityMapper.toDTO(specialityEnt);

        log.info("User: {}, created a new speciality: {}", securityHelper.getCurrentUsername(), specialityDTO);

        return specialityDTO;
    }


    @Override
    @Transactional
    public SpecialityDTO update(
            long id,
            UpdateSpecialityReq updateSpecialityReq
    ) {
        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(id);

        specialityMapper.updateEntityFromDto(updateSpecialityReq, specialityEnt);

        SpecialityDTO specialityDTO = specialityMapper.toDTO(specialityEnt);

        log.info("User: {}, updated a speciality: {} with data: {}", securityHelper.getCurrentUsername(), id, specialityDTO);

        return specialityDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(id);

        try {
            specialityRepo.delete(specialityEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting speciality: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting speciality");
        }

        log.info("User: {}, deleted a speciality: {} with data: {}", securityHelper.getCurrentUsername(), id, specialityEnt);
    }
}
