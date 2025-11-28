package ru.ai.sin.service.impl;

import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;

import java.util.List;
import java.util.UUID;

public interface PortfolioService {

    // ---------- GET METHODS ----------
    PortfolioDTO getById(long id);

    List<PortfolioDTO> getAll(long page, long size);
    List<PortfolioDTO> getAllByStudentId(UUID studentId,  long page, long size);

    // ---------- POST METHODS ----------
    PortfolioDTO create(AddPortfolioReq  addPortfolioReq);
    PortfolioDTO update(long id, AddPortfolioReq  addPortfolioReq);

    // ---------- DELETE METHODS ----------
    PortfolioDTO deleteById(long id);
}
