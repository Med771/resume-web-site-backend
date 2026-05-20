package ru.ai.sin.logic.siteproject;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SiteProjectRepo extends JpaRepository<SiteProjectEnt, UUID> {

    List<SiteProjectEnt> findAllByOrderBySortOrderAsc();
}
