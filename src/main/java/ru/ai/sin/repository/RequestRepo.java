package ru.ai.sin.repository;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.RequestEnt;

@Repository
public interface RequestRepo extends JpaRepository<RequestEnt, Long> {

    @EntityGraph(attributePaths = {"recruiter", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    RequestEnt findById(long id);

    @EntityGraph(attributePaths = {"recruiter", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    org.springframework.data.domain.Page<RequestEnt> findAll(Specification<RequestEnt> spec, org.springframework.data.domain.Pageable pageable);
}
