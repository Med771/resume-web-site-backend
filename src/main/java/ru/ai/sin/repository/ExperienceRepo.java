package ru.ai.sin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.ExperienceEnt;

import java.util.List;

@Repository
public interface ExperienceRepo extends JpaRepository<ExperienceEnt, Long> {

    List<ExperienceEnt> findAllByCompanyId(Long companyId);
}
