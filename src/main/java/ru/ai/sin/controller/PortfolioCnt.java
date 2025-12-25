package ru.ai.sin.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.service.impl.PortfolioService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/portfolio")
public class PortfolioCnt {

    private final PortfolioService portfolioService;

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/getById/{id}")
    public ResponseEntity<PortfolioDTO> getById(
            @PathVariable long id
    ) {
        PortfolioDTO portfolioDTO = portfolioService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "/getAll")
    public ResponseEntity<List<PortfolioDTO>> getAll(
            @Min(0) @RequestParam(defaultValue = "0") int pagePortfolioNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pagePortfolioSize
    ) {
        List<PortfolioDTO> portfolioDTOs = portfolioService.getAll(
                pagePortfolioNumber, pagePortfolioSize);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTOs);
    }

    @PreAuthorize("hasAnyRole('GUEST', 'USER', 'ADMIN')")
    @GetMapping(path = "getAllByStudentId/{studentId}")
    public ResponseEntity<List<PortfolioDTO>> getAllByStudentId(
            @PathVariable UUID studentId,

            @Min(0) @RequestParam(defaultValue = "0") int pagePortfolioNumber,
            @Min(1) @RequestParam(defaultValue = "10") int pagePortfolioSize
    ) {
        List<PortfolioDTO> portfolioDTOs = portfolioService.getAllByStudentId(
                studentId,
                pagePortfolioNumber, pagePortfolioSize);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTOs);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/create")
    public ResponseEntity<PortfolioDTO> create(
            @Valid @RequestBody AddPortfolioReq portfolioReq
    ) {
        PortfolioDTO portfolioDTO = portfolioService.create(portfolioReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/updateById/{id}")
    public ResponseEntity<PortfolioDTO> update(
            @PathVariable long id,

            @Valid @RequestBody AddPortfolioReq portfolioReq
    ) {
        PortfolioDTO portfolioDTO = portfolioService.update(id, portfolioReq);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(path = "/deleteById/{id}")
    public ResponseEntity<PortfolioDTO> deleteById(
            @PathVariable long id
    ) {
        PortfolioDTO portfolioDTO = portfolioService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }
}
