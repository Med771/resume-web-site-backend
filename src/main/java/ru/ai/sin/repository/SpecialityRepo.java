package ru.ai.sin.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import ru.ai.sin.entity.SpecialityEnt;

@Repository
public interface SpecialityRepo extends JpaRepository<SpecialityEnt, Long> {

    Page<SpecialityEnt> findAll(Specification<SpecialityEnt> spec, Pageable pageable);
}
