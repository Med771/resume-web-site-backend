package ru.ai.sin.logic.siteproject;

import ru.ai.sin.logic.siteproject.dto.CreateSiteProjectReq;
import ru.ai.sin.logic.siteproject.dto.ReorderSiteProjectsReq;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;
import ru.ai.sin.logic.siteproject.dto.SiteProjectStudentsReq;
import ru.ai.sin.logic.siteproject.dto.UpdateSiteProjectReq;

import java.util.List;
import java.util.UUID;

public interface SiteProjectService {

    List<SiteProjectDTO> listAdminOrdered(String findString);

    SiteProjectDTO getAdminById(UUID id);

    List<SiteProjectDTO> listPublicVisible(String findString);

    SiteProjectDTO getPublicVisibleById(UUID id);

    List<SiteProjectDTO> listAuthenticatedVisible(boolean includeStudents, String findString);

    SiteProjectDTO getAuthenticatedVisibleById(UUID id, boolean includeStudents);

    SiteProjectDTO create(CreateSiteProjectReq req);

    SiteProjectDTO update(UUID id, UpdateSiteProjectReq req);

    void delete(UUID id);

    void reorder(ReorderSiteProjectsReq req);

    List<UUID> listStudentIds(UUID projectId);

    void bindStudents(UUID projectId, SiteProjectStudentsReq req);

    void unbindStudents(UUID projectId, SiteProjectStudentsReq req);
}
