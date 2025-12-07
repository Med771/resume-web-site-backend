package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.CompanyEnt;

@Repository
public interface CompanyRepo extends JpaRepository<CompanyEnt, Long> {

    CompanyEnt findByIdAndIsActiveTrue(Long id);

    Page<CompanyEnt> findAllByNameIgnoreCaseAndIsActiveTrue(String name, Pageable pageable);

    Page<CompanyEnt> findAllByIsActiveTrue(Pageable pageable);
}
