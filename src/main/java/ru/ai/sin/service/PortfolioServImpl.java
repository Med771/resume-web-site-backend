package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.repository.PortfolioRepo;
import ru.ai.sin.service.impl.PortfolioService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PortfolioServImpl implements PortfolioService {

    private final PortfolioRepo  portfolioRepo;

    @Override
    public PortfolioDTO getById(long id) {
        return null;
    }

    @Override
    public List<PortfolioDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public List<PortfolioDTO> getAllByStudentId(UUID studentId, long page, long size) {
        return List.of();
    }

    @Override
    public PortfolioDTO create(AddPortfolioReq addPortfolioReq) {
        return null;
    }

    @Override
    public PortfolioDTO update(long id, AddPortfolioReq addPortfolioReq) {
        return null;
    }

    @Override
    public PortfolioDTO deleteById(long id) {
        return null;
    }
}
