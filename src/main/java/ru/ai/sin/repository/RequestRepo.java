package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.ai.sin.entity.RequestEnt;

@Repository
public interface RequestRepo extends JpaRepository<RequestEnt, Long>, JpaSpecificationExecutor<RequestEnt> {

    @EntityGraph(attributePaths = {"recruiter", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    RequestEnt findById(long id);

    @NonNull
    @EntityGraph(attributePaths = {"recruiter", "student"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<RequestEnt> findAll(Specification<RequestEnt> spec,
                             @NonNull Pageable pageable);
}
