package ru.ai.sin.logic.siteproject;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SiteProjectRepo extends JpaRepository<SiteProjectEnt, UUID> {

    List<SiteProjectEnt> findAllByOrderBySortOrderAsc();

    @EntityGraph(attributePaths = "students", type = EntityGraph.EntityGraphType.LOAD)
    Optional<SiteProjectEnt> findWithStudentsById(UUID id);

    @Query("""
            SELECT s.id FROM SiteProjectEnt p JOIN p.students s WHERE p.id = :projectId
            ORDER BY s.userInformation.lastName, s.userInformation.firstName, s.id
            """)
    List<UUID> findStudentIdsByProjectId(@Param("projectId") UUID projectId);
}
