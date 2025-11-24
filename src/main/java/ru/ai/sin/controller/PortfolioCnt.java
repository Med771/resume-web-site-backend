package ru.ai.sin.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.GetPortfolioRes;
import ru.ai.sin.dto.portfolio.MergePortfolioReq;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/portfolio")
public class PortfolioCnt {

    @GetMapping(path = "/getById")
    public ResponseEntity<GetPortfolioRes> getById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<GetPortfolioRes>> getAll(
            @RequestParam long page,
            @RequestParam long size) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<GetPortfolioRes> create(
            @RequestBody AddPortfolioReq portfolioReq) {
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @PutMapping(path = "/merge")
    public ResponseEntity<GetPortfolioRes> merge(
            @RequestParam long id,
            @RequestBody MergePortfolioReq portfolioReq) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<GetPortfolioRes> deleteById(
            @RequestParam long id) {
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
