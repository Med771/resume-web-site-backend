package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;

import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.dto.portfolio.PortfolioFilterReq;


public interface PortfolioService {

    // ---------- GET METHODS ----------
    PortfolioDTO getById(long id);

    // ---------- POST METHODS ----------
    PageResponse<PortfolioDTO> getAllByFilter(
            Pageable pageable,
            PortfolioFilterReq portfolioFilterReq);

    PortfolioDTO create(AddPortfolioReq  addPortfolioReq);
    PortfolioDTO update(
            long id,
            AddPortfolioReq  addPortfolioReq);

    // ---------- DELETE METHODS ----------
    void deleteById(long id);
}
