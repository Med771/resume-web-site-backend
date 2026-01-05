package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;

import ru.ai.sin.dto.portfolio.PortfolioFilterReq;
import ru.ai.sin.service.impl.PortfolioService;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<PortfolioDTO> getById(@PathVariable @Min(1) long id) {
        PortfolioDTO portfolioDTO = portfolioService.getById(id);

        return ResponseEntity.ok(portfolioDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @PostMapping(path = "/filter")
    public ResponseEntity<PageResponse<PortfolioDTO>> findAllByFilter(
            @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,

            @Valid @RequestBody PortfolioFilterReq portfolioFilterReq) {
        PageResponse<PortfolioDTO> portfolioDTOs = portfolioService.getAllByFilter(pageable, portfolioFilterReq);

        return ResponseEntity.ok(portfolioDTOs);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<PortfolioDTO> create(@Valid @RequestBody AddPortfolioReq portfolioReq) {
        PortfolioDTO portfolioDTO = portfolioService.create(portfolioReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/{id}")
    public ResponseEntity<PortfolioDTO> update(
            @PathVariable @Min(1) long id,

            @Valid @RequestBody AddPortfolioReq portfolioReq
    ) {
        PortfolioDTO portfolioDTO = portfolioService.update(id, portfolioReq);

        return ResponseEntity.ok(portfolioDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable @Min(1) long id) {
        portfolioService.deleteById(id);
    }
}
