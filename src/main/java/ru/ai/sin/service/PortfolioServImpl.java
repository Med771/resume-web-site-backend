package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.entity.PortfolioEnt;
import ru.ai.sin.entity.StudentEnt;

import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.PortfolioMapper;
import ru.ai.sin.repository.PortfolioRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.PortfolioService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioServImpl implements PortfolioService {

    private final PortfolioRepo portfolioRepo;
    private final StudentRepo studentRepo;

    private final PortfolioMapper portfolioMapper;

    private PortfolioEnt getActivePortfolioOrThrow(long id) {
        PortfolioEnt portfolioEnt = portfolioRepo.findByIdAndIsActiveTrue(id);

        if (portfolioEnt == null) {
            throw new NotFoundException("Failed to find portfolio by id " + id);
        }

        return portfolioEnt;
    }

    private StudentEnt getActiveStudentOrThrow(UUID id) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(id);

        if (studentEnt == null) {
            throw new NotFoundException("Failed to find student by id " + id);
        }

        return studentEnt;
    }

    @Override
    @Transactional(readOnly = true)
    public PortfolioDTO getById(
            long id
    ) {
       PortfolioEnt portfolioEnt = getActivePortfolioOrThrow(id);

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDTO> getAll(
            int pagePortfolioNumber, int pagePortfolioSize
    ) {
        Pageable pageable = PageRequest.of(pagePortfolioNumber, pagePortfolioSize);

        List<PortfolioEnt> portfolioEntList = portfolioRepo.findAllByIsActiveTrue(pageable).getContent();

        return portfolioEntList.stream()
                .map(portfolioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDTO> getAllByStudentId(
            UUID studentId,
            int pagePortfolioNumber, int pagePortfolioSize
    ) {
        Pageable pageable = PageRequest.of(pagePortfolioNumber, pagePortfolioSize);

        List<PortfolioEnt> portfolioEntList = portfolioRepo.findAllByStudentIdAndIsActiveTrue(
                studentId, pageable).getContent();

        return portfolioEntList.stream()
                .map(portfolioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PortfolioDTO create(
            AddPortfolioReq addPortfolioReq
    ) {
        StudentEnt studentEnt = getActiveStudentOrThrow(addPortfolioReq.studentId());

        PortfolioEnt portfolioEnt = portfolioMapper.toEntity(addPortfolioReq, studentEnt);

        portfolioEnt = portfolioRepo.save(portfolioEnt);

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
    @Transactional
    public PortfolioDTO update(
            long id,
            AddPortfolioReq addPortfolioReq) {
        PortfolioEnt portfolioEnt = portfolioRepo.findWithStudentById(id);

        if (portfolioEnt == null) {
            throw new NotFoundException("Failed to find portfolio by id " + id);
        }

        StudentEnt studentEnt = getActiveStudentOrThrow(addPortfolioReq.studentId());

        portfolioMapper.updateEntityFromDto(addPortfolioReq, portfolioEnt);

        portfolioEnt.setStudent(studentEnt);

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
    @Transactional
    public PortfolioDTO deleteById(
            long id
    ) {
        PortfolioEnt portfolioEnt = getActivePortfolioOrThrow(id);

        portfolioEnt.setIsActive(false);

        return portfolioMapper.toDTO(portfolioEnt);
    }
}
