package ru.ai.sin.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import ru.ai.sin.entity.CategoryEnt;

import java.util.List;
import java.util.Optional;

public interface CategoryRepo extends CrudRepository<CategoryEnt, Long> {

    @EntityGraph(attributePaths = {"skills"})
    Optional<CategoryEnt> findByIdAndIsActiveTrue(Long id);

    @EntityGraph(attributePaths = {"skills"})
    Optional<CategoryEnt> findByNameIgnoreCase(String name);

    @EntityGraph(attributePaths = {"skills"})
    List<CategoryEnt> findAllByIsActiveTrue(PageRequest pageRequest);
}
