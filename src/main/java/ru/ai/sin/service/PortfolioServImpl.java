package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.dto.portfolio.PortfolioFilterReq;

import ru.ai.sin.entity.PortfolioEnt;
import ru.ai.sin.entity.StudentEnt;

import ru.ai.sin.entity.spec.PortfolioSpecifications;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.mapper.PortfolioMapper;

import ru.ai.sin.repository.PortfolioRepo;

import ru.ai.sin.service.impl.PortfolioService;

import ru.ai.sin.service.tools.PortfolioTools;
import ru.ai.sin.service.tools.StudentTools;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioServImpl implements PortfolioService {

    private final PortfolioRepo portfolioRepo;

    private final PortfolioMapper portfolioMapper;

    private final PortfolioTools portfolioTools;
    private final StudentTools studentTools;

    private final SecurityHelper securityHelper;

    @Override
    public PortfolioDTO getById(long id) {
        return portfolioMapper.toDTO(portfolioTools.getPortfolioOrThrow(id));
    }

    @Override
    public PageResponse<PortfolioDTO> getAllByFilter(Pageable pageable, PortfolioFilterReq portfolioFilterReq) {
        Page<PortfolioEnt> page = portfolioRepo.findAll(
                PortfolioSpecifications.byFilters(portfolioFilterReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(portfolioMapper::toDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    @Transactional
    public PortfolioDTO create(AddPortfolioReq addPortfolioReq) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(addPortfolioReq.studentId());

        PortfolioEnt portfolioEnt = portfolioMapper.toEntity(addPortfolioReq, studentEnt);

        try {
            portfolioEnt = portfolioRepo.save(portfolioEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Portfolio already exists: {}", addPortfolioReq.name());

            throw new BadRequestException("Portfolio already exists: " + addPortfolioReq.name());
        }

        PortfolioDTO portfolioDTO = portfolioMapper.toDTO(portfolioEnt);

        log.info("User: {}, created a new portfolio: {}", securityHelper.getCurrentUsername(), portfolioDTO);

        return portfolioDTO;
    }

    @Override
    @Transactional
    public PortfolioDTO update(
            long id,
            AddPortfolioReq addPortfolioReq
    ) {
        PortfolioEnt portfolioEnt = portfolioTools.getPortfolioOrThrow(id);
        StudentEnt studentEnt = studentTools.getStudentOrThrow(addPortfolioReq.studentId());

        portfolioMapper.updateEntityFromDto(addPortfolioReq, portfolioEnt);

        portfolioEnt.setStudent(studentEnt);

        PortfolioDTO portfolioDTO = portfolioMapper.toDTO(portfolioEnt);

        log.info("User: {}, updated a portfolio: {} with data: {}", securityHelper.getCurrentUsername(), id, portfolioDTO);

        return portfolioDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        PortfolioEnt portfolioEnt = portfolioTools.getPortfolioOrThrow(id);

        try {
            portfolioRepo.delete(portfolioEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting portfolio: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting portfolio");
        }

        log.info("User: {}, deleted a portfolio: {} with data: {}", securityHelper.getCurrentUsername(), id, portfolioEnt);
    }
}
