package ru.ai.sin.logic.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import ru.ai.sin.logic.skill.SkillEnt;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface StudentRepo extends
        JpaRepository<StudentEnt, UUID>,
        JpaSpecificationExecutor<StudentEnt>  {

    @NonNull
    @EntityGraph(attributePaths = {"speciality", "skills"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<StudentEnt> findById(@NonNull UUID id);

    @NonNull
    @EntityGraph(attributePaths = {"speciality", "skills"}, type = EntityGraph.EntityGraphType.LOAD)
    Page<StudentEnt> findAll(Specification<StudentEnt> specification, @NonNull Pageable pageable);

    @Query("SELECT s.skills FROM StudentEnt s WHERE s.id = :studentId")
    Set<SkillEnt> findSkillsByStudentId(UUID studentId);

    Optional<StudentEnt> findByContactInformationTelegramUserId(String telegramUserId);

    @Query("""
            SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
            FROM StudentEnt s
            WHERE LOWER(TRIM(s.userInformation.email)) = LOWER(TRIM(:email))
            """)
    boolean existsByNormalizedEmail(@Param("email") String email);

    @Query("select count(s) from StudentEnt s")
    long countAllStudents();

    @Query("""
            select count(s) from StudentEnt s
            where s.timestamps.createdAt is not null
              and s.timestamps.createdAt >= :from
              and s.timestamps.createdAt < :to""")
    long countStudentsCreatedBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
