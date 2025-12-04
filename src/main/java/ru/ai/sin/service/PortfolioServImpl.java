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
import ru.ai.sin.exception.models.BadRequestException;
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

    @Override
    public PortfolioDTO getById(
            long id
    ) {
        PortfolioEnt portfolioEnt = portfolioRepo.findByIdAndIsActiveTrue(id);

        if (portfolioEnt == null) {
            throw new BadRequestException("Failed to find portfolio by id " + id);
        }

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
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
        PortfolioEnt portfolioEnt = portfolioRepo.findByLinkAndIsActiveTrue(addPortfolioReq.link());
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(addPortfolioReq.studentId());

        if (studentEnt == null) {
            throw new BadRequestException("Failed to find student by id " + addPortfolioReq.studentId());
        }

        if (portfolioEnt == null) {
            portfolioEnt = portfolioMapper.toEntity(addPortfolioReq, studentEnt);
        }
        else {
            portfolioEnt.setName(addPortfolioReq.name());
            portfolioEnt.setAdditionalInfo(addPortfolioReq.additionalInfo());
            portfolioEnt.setStudent(studentEnt);
        }

        portfolioEnt = portfolioRepo.save(portfolioEnt);

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
    public PortfolioDTO update(
            long id,
            AddPortfolioReq addPortfolioReq) {
        PortfolioEnt portfolioEnt = portfolioRepo.findWithStudentById(id);
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(addPortfolioReq.studentId());

        if (studentEnt == null) {
            throw new BadRequestException("Failed to find student by id " + addPortfolioReq.studentId());
        }

        if (portfolioEnt == null) {
            portfolioEnt = portfolioMapper.toEntity(addPortfolioReq, studentEnt);
        }

        portfolioEnt.setAdditionalInfo(addPortfolioReq.additionalInfo());
        portfolioEnt.setName(addPortfolioReq.name());
        portfolioEnt.setIsActive(true);
        portfolioEnt.setStudent(studentEnt);

        portfolioEnt = portfolioRepo.save(portfolioEnt);

        return portfolioMapper.toDTO(portfolioEnt);
    }

    @Override
    public PortfolioDTO deleteById(
            long id
    ) {
        PortfolioEnt portfolioEnt = portfolioRepo.findByIdAndIsActiveTrue(id);

        if (portfolioEnt == null) {
            throw new BadRequestException("Failed to find portfolio by id " + id);
        }

        portfolioEnt.setIsActive(false);

        portfolioRepo.save(portfolioEnt);

        return portfolioMapper.toDTO(portfolioEnt);
    }
}
