package ru.ai.sin.logic.siteproject;

import ru.ai.sin.logic.siteproject.dto.CreateSiteProjectReq;
import ru.ai.sin.logic.siteproject.dto.ReorderSiteProjectsReq;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;
import ru.ai.sin.logic.siteproject.dto.UpdateSiteProjectReq;

import java.util.List;
import java.util.UUID;

public interface SiteProjectService {

    List<SiteProjectDTO> listAdminOrdered();

    List<SiteProjectDTO> listPublicVisible();

    SiteProjectDTO create(CreateSiteProjectReq req);

    SiteProjectDTO update(UUID id, UpdateSiteProjectReq req);

    void delete(UUID id);

    void reorder(ReorderSiteProjectsReq req);
}
