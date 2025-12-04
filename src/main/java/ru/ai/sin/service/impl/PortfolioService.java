package ru.ai.sin.service.impl;

import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;

import java.util.List;
import java.util.UUID;

public interface PortfolioService {

    // ---------- GET METHODS ----------
    PortfolioDTO getById(
            long id);

    List<PortfolioDTO> getAll(
            int pagePortfolioNumber, int pagePortfolioSize);
    List<PortfolioDTO> getAllByStudentId(
            UUID studentId,
            int pagePortfolioNumber, int pagePortfolioSize);

    // ---------- POST METHODS ----------
    PortfolioDTO create(
            AddPortfolioReq  addPortfolioReq);
    PortfolioDTO update(
            long id,
            AddPortfolioReq  addPortfolioReq);

    // ---------- DELETE METHODS ----------
    PortfolioDTO deleteById(
            long id);
}
