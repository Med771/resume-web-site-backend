package ru.ai.sin.logic.siteproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.siteproject.dto.CreateSiteProjectReq;
import ru.ai.sin.logic.siteproject.dto.ReorderSiteProjectsReq;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;
import ru.ai.sin.logic.siteproject.dto.SiteProjectStudentsReq;
import ru.ai.sin.logic.siteproject.dto.UpdateSiteProjectReq;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentRepo;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteProjectServiceImpl implements SiteProjectService {

    private final SiteProjectRepo siteProjectRepo;
    private final StudentRepo studentRepo;

    @Override
    @Transactional(readOnly = true)
    public List<SiteProjectDTO> listAdminOrdered() {
        return siteProjectRepo.findAllByOrderBySortOrderAsc().stream().map(this::toDto).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteProjectDTO> listPublicVisible() {
        return projectsInPublicationWindow().stream()
                .filter(SiteProjectEnt::isVisibleToAnonymous)
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteProjectDTO> listAuthenticatedVisible() {
        return projectsInPublicationWindow().stream()
                .map(this::toDto)
                .toList();
    }

    private List<SiteProjectEnt> projectsInPublicationWindow() {
        LocalDateTime now = LocalDateTime.now();
        return siteProjectRepo.findAllByOrderBySortOrderAsc().stream()
                .filter(p -> p.getPublishedFrom() == null || !p.getPublishedFrom().isAfter(now))
                .filter(p -> p.getPublishedTo() == null || !p.getPublishedTo().isBefore(now))
                .toList();
    }

    @Override
    @Transactional
    public SiteProjectDTO create(CreateSiteProjectReq req) {
        int nextOrder = siteProjectRepo.findAllByOrderBySortOrderAsc().stream()
                .mapToInt(SiteProjectEnt::getSortOrder)
                .max()
                .orElse(-1) + 1;
        SiteProjectEnt e = new SiteProjectEnt();
        e.setTitle(req.title());
        e.setSummary(req.summary());
        e.setBody(req.body());
        e.setImagePath(req.imagePath());
        e.setVisibleToAnonymous(req.visibleToAnonymous());
        e.setPublishedFrom(req.publishedFrom());
        e.setPublishedTo(req.publishedTo());
        e.setSortOrder(nextOrder);
        return toDto(siteProjectRepo.save(e));
    }

    @Override
    @Transactional
    public SiteProjectDTO update(UUID id, UpdateSiteProjectReq req) {
        SiteProjectEnt e = siteProjectRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id));
        e.setTitle(req.title());
        e.setSummary(req.summary());
        e.setBody(req.body());
        e.setImagePath(req.imagePath());
        e.setVisibleToAnonymous(req.visibleToAnonymous());
        e.setPublishedFrom(req.publishedFrom());
        e.setPublishedTo(req.publishedTo());
        return toDto(siteProjectRepo.save(e));
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        if (!siteProjectRepo.existsById(id)) {
            throw new NotFoundException("Project not found: " + id);
        }
        siteProjectRepo.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> listStudentIds(UUID projectId) {
        ensureProjectExists(projectId);
        return siteProjectRepo.findStudentIdsByProjectId(projectId);
    }

    @Override
    @Transactional
    public void bindStudents(UUID projectId, SiteProjectStudentsReq req) {
        List<UUID> studentIds = validateUniqueStudentIds(req.studentIds());
        SiteProjectEnt project = siteProjectRepo.findWithStudentsById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found: " + projectId));
        Set<StudentEnt> toBind = resolveStudentsOrThrow(studentIds);
        project.getStudents().addAll(toBind);
        siteProjectRepo.save(project);
    }

    @Override
    @Transactional
    public void unbindStudents(UUID projectId, SiteProjectStudentsReq req) {
        List<UUID> studentIds = validateUniqueStudentIds(req.studentIds());
        SiteProjectEnt project = siteProjectRepo.findWithStudentsById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found: " + projectId));
        Set<UUID> toRemove = new HashSet<>(studentIds);
        project.getStudents().removeIf(s -> toRemove.contains(s.getId()));
        siteProjectRepo.save(project);
    }

    @Override
    @Transactional
    public void reorder(ReorderSiteProjectsReq req) {
        List<UUID> ids = req.orderedIds();
        Set<UUID> unique = new HashSet<>(ids);
        if (unique.size() != ids.size()) {
            throw new BadRequestException("Duplicate ids in reorder list");
        }
        for (int i = 0; i < ids.size(); i++) {
            UUID id = ids.get(i);
            SiteProjectEnt e = siteProjectRepo.findById(id)
                    .orElseThrow(() -> new NotFoundException("Project not found: " + id));
            e.setSortOrder(i);
            siteProjectRepo.save(e);
        }
    }

    private void ensureProjectExists(UUID projectId) {
        if (!siteProjectRepo.existsById(projectId)) {
            throw new NotFoundException("Project not found: " + projectId);
        }
    }

    private static List<UUID> validateUniqueStudentIds(List<UUID> studentIds) {
        Set<UUID> unique = new HashSet<>(studentIds);
        if (unique.size() != studentIds.size()) {
            throw new BadRequestException("Duplicate ids in student list");
        }
        return studentIds;
    }

    private Set<StudentEnt> resolveStudentsOrThrow(List<UUID> studentIds) {
        List<StudentEnt> found = studentRepo.findAllById(studentIds);
        if (found.size() != studentIds.size()) {
            throw new NotFoundException("Some students were not found by ids");
        }
        return new HashSet<>(found);
    }

    private SiteProjectDTO toDto(SiteProjectEnt e) {
        return new SiteProjectDTO(
                e.getId(),
                e.getTitle(),
                e.getSummary(),
                e.getBody(),
                e.getImagePath(),
                e.getSortOrder(),
                e.isVisibleToAnonymous(),
                e.getPublishedFrom(),
                e.getPublishedTo()
        );
    }
}
