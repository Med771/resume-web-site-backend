package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ai.sin.entity.PortfolioEnt;

public interface PortfolioRepo extends JpaRepository<PortfolioEnt, Long> {
}
