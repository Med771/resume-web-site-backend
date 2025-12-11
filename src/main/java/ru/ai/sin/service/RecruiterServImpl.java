package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterNameReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;

import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.RecruiterMapper;
import ru.ai.sin.repository.RecruiterRepo;

import ru.ai.sin.service.impl.RecruiterService;
import ru.ai.sin.service.tools.RecruiterTools;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecruiterServImpl implements RecruiterService {

    private final RecruiterRepo recruiterRepo;

    private final RecruiterMapper recruiterMapper;

    private final RecruiterTools recruiterTools;

    @Override
    public RecruiterDTO getById(UUID id) {
        return recruiterMapper.toDTO(recruiterTools.getRecruiterOrThrow(id));
    }

    @Override
    public List<RecruiterDTO> getAll(
            int pageRecruiterNumber,
            int pageRecruiterSize
    ) {
        List<RecruiterEnt> recruiterEntList = recruiterRepo
                .findAll(PageRequest.of(pageRecruiterNumber, pageRecruiterSize))
                .getContent();

        return recruiterEntList.stream().map(recruiterMapper::toDTO).toList();
    }

    @Override
    public List<RecruiterDTO> getAllByCompanyName(
            int pageRecruiterNumber,
            int pageRecruiterSize,
            GetRecruiterNameReq getRecruiterNameReq
    ) {
        List<RecruiterEnt> recruiterEntList = recruiterRepo
                .findAllByCompanyNameIgnoreCase(
                        getRecruiterNameReq.companyName(),
                        PageRequest.of(pageRecruiterNumber, pageRecruiterSize))
                .getContent();

        return recruiterEntList.stream().map(recruiterMapper::toDTO).toList();
    }

    @Override
    public RecruiterDTO create(AddRecruiterReq addRecruiterReq) {
        RecruiterEnt recruiterEnt = recruiterMapper.toEntity(addRecruiterReq);

        try {
            recruiterEnt = recruiterRepo.save(recruiterEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Recruiter already exists: {}, {}", addRecruiterReq.email(), addRecruiterReq.telegramUsername());

            throw new BadRequestException("Recruiter already exists: %s, %s"
                    .formatted(addRecruiterReq.email(), addRecruiterReq.telegramUsername()));
        }

        return recruiterMapper.toDTO(recruiterEnt);
    }

    @Override
    @Transactional
    public RecruiterDTO update(
            UUID id,
            UpdateRecruiterReq updateRecruiterReq
    ) {
        RecruiterEnt recruiterEnt = recruiterTools.getRecruiterOrThrow(id);

        recruiterMapper.updateEntityFromDto(updateRecruiterReq, recruiterEnt);

        return recruiterMapper.toDTO(recruiterEnt);
    }

    @Override
    @Transactional
    public RecruiterDTO deleteById(UUID id) {
        RecruiterEnt recruiterEnt = recruiterTools.getRecruiterOrThrow(id);

        try {
            recruiterRepo.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting recruiter: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting recruiter");
        }

        return recruiterMapper.toDTO(recruiterEnt);
    }
}
