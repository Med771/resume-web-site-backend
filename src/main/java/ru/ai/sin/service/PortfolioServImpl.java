package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;

import ru.ai.sin.entity.PortfolioEnt;
import ru.ai.sin.entity.StudentEnt;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.mapper.PortfolioMapper;
import ru.ai.sin.repository.PortfolioRepo;

import ru.ai.sin.service.impl.PortfolioService;
import ru.ai.sin.service.tools.PortfolioTools;
import ru.ai.sin.service.tools.StudentTools;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioServImpl implements PortfolioService {

    private final PortfolioRepo portfolioRepo;

    private final PortfolioMapper portfolioMapper;

    private final PortfolioTools portfolioTools;
    private final StudentTools studentTools;

    @Override
    public PortfolioDTO getById(long id) {
        return portfolioMapper.toDTO(portfolioTools.getPortfolioOrThrow(id));
    }

    @Override
    public List<PortfolioDTO> getAll(
            int pagePortfolioNumber,
            int pagePortfolioSize
    ) {
        List<PortfolioEnt> portfolioEntList = portfolioRepo
                .findAll(
                        PageRequest.of(pagePortfolioNumber, pagePortfolioSize))
                .getContent();

        return portfolioEntList.stream()
                .map(portfolioMapper::toDTO)
                .toList();
    }

    @Override
    public List<PortfolioDTO> getAllByStudentId(
            UUID studentId,
            int pagePortfolioNumber,
            int pagePortfolioSize
    ) {
        List<PortfolioEnt> portfolioEntList = portfolioRepo
                .findAllByStudentId(
                        studentId,
                        PageRequest.of(pagePortfolioNumber, pagePortfolioSize))
                .getContent();

        return portfolioEntList.stream()
                .map(portfolioMapper::toDTO)
                .toList();
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

        return portfolioMapper.toDTO(portfolioEnt);
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

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
    @Transactional
    public PortfolioDTO deleteById(long id) {
        PortfolioEnt portfolioEnt = portfolioTools.getPortfolioOrThrow(id);

        try {
            portfolioRepo.delete(portfolioEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting portfolio: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting portfolio");
        }

        return portfolioMapper.toDTO(portfolioEnt);
    }
}
