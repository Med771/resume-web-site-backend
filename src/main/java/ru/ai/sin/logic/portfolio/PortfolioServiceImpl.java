package ru.ai.sin.logic.portfolio;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.models.PageResponse;
import ru.ai.sin.logic.portfolio.dto.AddPortfolioReq;
import ru.ai.sin.logic.portfolio.dto.PortfolioDTO;
import ru.ai.sin.logic.portfolio.dto.FilterPortfolioReq;

import ru.ai.sin.logic.student.StudentEnt;

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.tools.PortfolioTools;
import ru.ai.sin.tools.StudentTools;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

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
    public PageResponse<PortfolioDTO> getAllByFilter(Pageable pageable, FilterPortfolioReq filterPortfolioReq) {
        Page<PortfolioEnt> page = portfolioRepo.findAll(
                PortfolioSpecifications.byFilters(filterPortfolioReq),
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

        portfolioEnt = portfolioRepo.save(portfolioEnt);

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

        portfolioRepo.delete(portfolioEnt);

        log.info("User: {}, deleted a portfolio: {} with data: {}", securityHelper.getCurrentUsername(), id, portfolioEnt);
    }
}
