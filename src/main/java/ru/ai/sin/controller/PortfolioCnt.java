package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.service.impl.PortfolioService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/portfolio")
public class PortfolioCnt {

    private final PortfolioService portfolioService;

    @GetMapping(path = "/getById")
    public ResponseEntity<PortfolioDTO> getById(
            @RequestParam long id
    ) {
        PortfolioDTO portfolioDTO = portfolioService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<PortfolioDTO>> getAll(
            @RequestParam(defaultValue = "0") int pagePortfolioNumber,
            @RequestParam(defaultValue = "10") int pagePortfolioSize
    ) {
        List<PortfolioDTO> portfolioDTOs = portfolioService.getAll(
                pagePortfolioNumber, pagePortfolioSize);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTOs);
    }

    @GetMapping(path = "getAllByStudentId")
    public ResponseEntity<List<PortfolioDTO>> getAllByStudentId(
            @RequestParam(defaultValue = "0") int pagePortfolioNumber,
            @RequestParam(defaultValue = "10") int pagePortfolioSize,
            @RequestParam UUID studentId
    ) {
        List<PortfolioDTO> portfolioDTOs = portfolioService.getAllByStudentId(
                studentId,
                pagePortfolioNumber, pagePortfolioSize);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<PortfolioDTO> create(
            @RequestBody AddPortfolioReq portfolioReq
    ) {
        PortfolioDTO portfolioDTO = portfolioService.create(portfolioReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(portfolioDTO);
    }

    @PutMapping(path = "/update")
    public ResponseEntity<PortfolioDTO> update(
            @RequestParam long id,
            @RequestBody AddPortfolioReq portfolioReq
    ) {
        PortfolioDTO portfolioDTO = portfolioService.update(id, portfolioReq);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<PortfolioDTO> deleteById(
            @RequestParam long id
    ) {
        PortfolioDTO portfolioDTO = portfolioService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(portfolioDTO);
    }
}
