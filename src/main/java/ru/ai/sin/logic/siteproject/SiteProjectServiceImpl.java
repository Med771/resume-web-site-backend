package ru.ai.sin.logic.siteproject;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.logic.siteproject.dto.CreateSiteProjectReq;
import ru.ai.sin.logic.siteproject.dto.ReorderSiteProjectsReq;
import ru.ai.sin.logic.siteproject.dto.SiteProjectDTO;
import ru.ai.sin.logic.siteproject.dto.SiteProjectImageDTO;
import ru.ai.sin.logic.siteproject.dto.SiteProjectImageReq;
import ru.ai.sin.logic.siteproject.dto.SiteProjectParticipantDTO;
import ru.ai.sin.logic.siteproject.dto.SiteProjectStudentsReq;
import ru.ai.sin.logic.siteproject.dto.UpdateSiteProjectReq;
import ru.ai.sin.logic.skill.SkillEnt;
import ru.ai.sin.logic.skill.SkillMapper;
import ru.ai.sin.logic.skill.SkillRepo;
import ru.ai.sin.logic.skill.dto.SkillDTO;
import ru.ai.sin.logic.speciality.SpecialityEnt;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentRepo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SiteProjectServiceImpl implements SiteProjectService {

    private final SiteProjectRepo siteProjectRepo;
    private final StudentRepo studentRepo;
    private final SkillRepo skillRepo;
    private final SkillMapper skillMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SiteProjectDTO> listAdminOrdered(String findString) {
        return siteProjectRepo.findAllWithDetailsByOrderBySortOrderAsc().stream()
                .filter(p -> matchesFindString(p, findString))
                .map(p -> toDto(p, true))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SiteProjectDTO getAdminById(UUID id) {
        SiteProjectEnt project = siteProjectRepo.findWithDetailsById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id));
        return toDto(project, true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteProjectDTO> listPublicVisible(String findString) {
        return siteProjectRepo.findAllWithImagesByOrderBySortOrderAsc().stream()
                .filter(SiteProjectEnt::isVisibleToAnonymous)
                .filter(this::isInPublicationWindow)
                .filter(p -> matchesFindString(p, findString))
                .map(p -> toDto(p, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SiteProjectDTO getPublicVisibleById(UUID id) {
        SiteProjectEnt project = siteProjectRepo.findWithImagesById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id));
        if (!project.isVisibleToAnonymous() || !isInPublicationWindow(project)) {
            throw new NotFoundException("Project not found: " + id);
        }
        return toDto(project, false);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SiteProjectDTO> listAuthenticatedVisible(boolean includeStudents, String findString) {
        if (includeStudents) {
            return siteProjectRepo.findAllWithDetailsByOrderBySortOrderAsc().stream()
                    .filter(this::isInPublicationWindow)
                    .filter(p -> matchesFindString(p, findString))
                    .map(p -> toDto(p, true))
                    .toList();
        }
        return siteProjectRepo.findAllWithImagesByOrderBySortOrderAsc().stream()
                .filter(this::isInPublicationWindow)
                .filter(p -> matchesFindString(p, findString))
                .map(p -> toDto(p, false))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SiteProjectDTO getAuthenticatedVisibleById(UUID id, boolean includeStudents) {
        SiteProjectEnt project = includeStudents
                ? siteProjectRepo.findWithDetailsById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id))
                : siteProjectRepo.findWithImagesById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id));
        if (!isInPublicationWindow(project)) {
            throw new NotFoundException("Project not found: " + id);
        }
        return toDto(project, includeStudents);
    }

    private boolean isInPublicationWindow(SiteProjectEnt project) {
        LocalDateTime now = LocalDateTime.now();
        return (project.getPublishedFrom() == null || !project.getPublishedFrom().isAfter(now))
                && (project.getPublishedTo() == null || !project.getPublishedTo().isBefore(now));
    }

    private boolean matchesFindString(SiteProjectEnt project, String findString) {
        if (findString == null || findString.isBlank()) {
            return true;
        }
        String needle = findString.trim().toLowerCase();
        return fieldContains(project.getTitle(), needle)
                || fieldContains(project.getSummary(), needle)
                || fieldContains(project.getBody(), needle)
                || fieldContains(project.getSection(), needle)
                || project.getSkills().stream()
                .anyMatch(skill -> fieldContains(skill.getName(), needle));
    }

    private static boolean fieldContains(String value, String needle) {
        return value != null && value.toLowerCase().contains(needle);
    }

    @Override
    @Transactional
    public SiteProjectDTO create(CreateSiteProjectReq req) {
        int nextOrder = siteProjectRepo.findAllWithImagesByOrderBySortOrderAsc().stream()
                .mapToInt(SiteProjectEnt::getSortOrder)
                .max()
                .orElse(-1) + 1;
        SiteProjectEnt e = new SiteProjectEnt();
        e.setTitle(req.title());
        e.setSection(blankToNull(req.section()));
        e.setSummary(req.summary());
        e.setBody(req.body());
        e.setVisibleToAnonymous(req.visibleToAnonymous());
        e.setPublishedFrom(req.publishedFrom());
        e.setPublishedTo(req.publishedTo());
        e.setSortOrder(nextOrder);
        applyImages(e, req.images());
        applySkills(e, req.skillIds());
        return toDto(siteProjectRepo.save(e), false);
    }

    @Override
    @Transactional
    public SiteProjectDTO update(UUID id, UpdateSiteProjectReq req) {
        SiteProjectEnt e = siteProjectRepo.findWithImagesById(id)
                .orElseThrow(() -> new NotFoundException("Project not found: " + id));
        e.setTitle(req.title());
        e.setSection(blankToNull(req.section()));
        e.setSummary(req.summary());
        e.setBody(req.body());
        e.setVisibleToAnonymous(req.visibleToAnonymous());
        e.setPublishedFrom(req.publishedFrom());
        e.setPublishedTo(req.publishedTo());
        applyImages(e, req.images());
        applySkills(e, req.skillIds());
        return toDto(siteProjectRepo.save(e), false);
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

    private void applyImages(SiteProjectEnt project, List<SiteProjectImageReq> imageReqs) {
        project.getImages().clear();
        if (imageReqs == null || imageReqs.isEmpty()) {
            return;
        }
        Set<String> seen = new HashSet<>();
        int fallbackOrder = 0;
        for (SiteProjectImageReq req : imageReqs) {
            validateImageItem(req);
            String path = blankToNull(req.imagePath());
            String url = blankToNull(req.imageUrl());
            if (!seen.add(path + "\0" + url)) {
                continue;
            }
            SiteProjectImageEnt image = new SiteProjectImageEnt();
            image.setProject(project);
            image.setImagePath(path);
            image.setImageUrl(url);
            image.setSortOrder(req.sortOrder() != null ? req.sortOrder() : fallbackOrder);
            project.getImages().add(image);
            fallbackOrder++;
        }
    }

    private static void validateImageItem(SiteProjectImageReq req) {
        boolean hasPath = req.imagePath() != null && !req.imagePath().isBlank();
        boolean hasUrl = req.imageUrl() != null && !req.imageUrl().isBlank();
        if (!hasPath && !hasUrl) {
            throw new BadRequestException("Each project image must have imagePath or imageUrl");
        }
    }

    private void applySkills(SiteProjectEnt project, List<Long> skillIds) {
        project.getSkills().clear();
        if (skillIds == null || skillIds.isEmpty()) {
            return;
        }
        project.getSkills().addAll(resolveSkillsByIds(skillIds));
    }

    private Set<SkillEnt> resolveSkillsByIds(List<Long> skillIds) {
        Set<Long> unique = new HashSet<>(skillIds);
        if (unique.size() != skillIds.size()) {
            throw new BadRequestException("Duplicate ids in skill list");
        }
        Set<SkillEnt> found = skillRepo.findAllByIdIn(unique);
        if (found.size() != unique.size()) {
            throw new NotFoundException("Some skills were not found by ids");
        }
        return found;
    }

    private static String blankToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
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

    private SiteProjectDTO toDto(SiteProjectEnt e, boolean includeStudents) {
        List<SiteProjectParticipantDTO> students = null;
        if (includeStudents) {
            students = e.getStudents().stream()
                    .sorted(Comparator
                            .comparing((StudentEnt s) -> s.getUserInformation().getLastName(), Comparator.nullsLast(String::compareToIgnoreCase))
                            .thenComparing(s -> s.getUserInformation().getFirstName(), Comparator.nullsLast(String::compareToIgnoreCase))
                            .thenComparing(StudentEnt::getId))
                    .map(this::toParticipant)
                    .toList();
        }
        List<SiteProjectImageDTO> images = mapUniqueImages(e.getImages());
        List<SkillDTO> skills = e.getSkills().stream()
                .sorted(Comparator.comparing(SkillEnt::getName, String.CASE_INSENSITIVE_ORDER))
                .map(skillMapper::toDTO)
                .toList();
        return new SiteProjectDTO(
                e.getId(),
                e.getTitle(),
                e.getSection(),
                e.getSummary(),
                e.getBody(),
                images,
                skills,
                e.getSortOrder(),
                e.isVisibleToAnonymous(),
                e.getPublishedFrom(),
                e.getPublishedTo(),
                students
        );
    }

    private static List<SiteProjectImageDTO> mapUniqueImages(List<SiteProjectImageEnt> images) {
        Set<UUID> seenIds = new HashSet<>();
        return images.stream()
                .sorted(Comparator.comparingInt(SiteProjectImageEnt::getSortOrder))
                .filter(img -> img.getId() != null && seenIds.add(img.getId()))
                .map(img -> new SiteProjectImageDTO(
                        img.getId(),
                        img.getImagePath(),
                        img.getImageUrl(),
                        img.getSortOrder()))
                .toList();
    }

    private SiteProjectParticipantDTO toParticipant(StudentEnt student) {
        SpecialityEnt speciality = student.getSpeciality();
        return new SiteProjectParticipantDTO(
                student.getId(),
                student.getUserInformation().getFirstName(),
                student.getUserInformation().getLastName(),
                student.getImagePath(),
                speciality != null ? speciality.getName() : null,
                student.getCourse()
        );
    }
}
