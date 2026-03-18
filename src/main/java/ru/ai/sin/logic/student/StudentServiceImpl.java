package ru.ai.sin.logic.student;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.ai.sin.models.PageResponse;
import ru.ai.sin.models.embeddables.ContactInformation;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.helper.FileHelper;
import ru.ai.sin.helper.SecurityHelper;
import ru.ai.sin.logic.company.CompanyEnt;
import ru.ai.sin.logic.company.CompanyRepo;
import ru.ai.sin.logic.education.EducationEnt;
import ru.ai.sin.logic.education.EducationRepo;
import ru.ai.sin.logic.experience.ExperienceEnt;
import ru.ai.sin.logic.experience.ExperienceRepo;
import ru.ai.sin.logic.institution.InstitutionEnt;
import ru.ai.sin.logic.institution.InstitutionRepo;
import ru.ai.sin.logic.portfolio.PortfolioEnt;
import ru.ai.sin.logic.portfolio.PortfolioRepo;
import ru.ai.sin.logic.student.dto.*;
import ru.ai.sin.logic.speciality.SpecialityEnt;
import ru.ai.sin.logic.skill.SkillEnt;
import ru.ai.sin.logic.skill.SkillRepo;
import ru.ai.sin.tools.SkillTools;
import ru.ai.sin.tools.SpecialityTools;
import ru.ai.sin.tools.StudentTools;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepo studentRepo;
    private final SkillRepo skillRepo;
    private final CompanyRepo companyRepo;
    private final EducationRepo educationRepo;
    private final PortfolioRepo portfolioRepo;
    private final ExperienceRepo experienceRepo;
    private final InstitutionRepo institutionRepo;

    private final StudentMapper studentMapper;

    private final StudentTools studentTools;
    private final SpecialityTools specialityTools;
    private final SkillTools skillTools;

    private final FileHelper fileHelper;
    private final SecurityHelper securityHelper;

    @Override
    public StudentDTO getById(UUID id) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        return studentTools.mapToDTO(studentEnt);
    }

    @Override
    @Transactional
    public void setPhoto(UUID id, MultipartFile file) {
        fileHelper.validateMultipart(file);

        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        String filePath = fileHelper.saveFile(file, studentEnt.getId().toString());

        if (filePath == null) {
            throw new BadRequestException("Failed to save file");
        }

        studentEnt.setImagePath(filePath);
    }

    @Override
    @Transactional
    public PageResponse<StudentCardDTO> getAllCardsByFilter(
            Pageable pageable,
            FilterStudentReq filterStudentReq
    ) {
        Page<StudentEnt> page = studentRepo.findAll(
                StudentSpecifications.byFilters(filterStudentReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(studentTools::mapToCardDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }

    @Override
    public PageResponse<StudentDTO> getAllByFilter(
            Pageable pageable,
            FilterStudentReq filterStudentReq
    ) {
        Page<StudentEnt> page = studentRepo.findAll(
                StudentSpecifications.byFilters(filterStudentReq),
                pageable);

        return new PageResponse<>(
                page.getContent().stream().map(studentTools::mapToDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }


    @Override
    @Transactional
    public StudentDTO create(AddStudentReq addStudentReq) {
        StudentEnt studentEnt = studentMapper.toEntity(addStudentReq);

        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(addStudentReq.specialityId());

        Set<SkillEnt> skillEntSet = resolveSkillsByIdsOrThrow(addStudentReq.skillsIds());

        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);

        try {
            studentEnt = studentRepo.save(studentEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Student already exists: {}, {}", addStudentReq.email(), addStudentReq.telegramUsername());

            throw new BadRequestException("Student already exists: %s, %s"
                    .formatted(addStudentReq.email(), addStudentReq.telegramUsername()));
        }

        StudentDTO studentDTO = studentTools.mapToDTO(studentEnt);

        log.info("User: {}, created a new student: {}", securityHelper.getCurrentUsername(), studentDTO);

        return studentDTO;
    }

    @Override
    @Transactional
    public StudentDTO createExtended(CreateStudentExtendedReq createStudentExtendedReq) {
        StudentEnt studentEnt = studentMapper.toEntity(toAddStudentReq(createStudentExtendedReq));
        studentEnt.setSpeciality(specialityTools.getSpecialityOrThrow(createStudentExtendedReq.specialityId()));
        studentEnt.setSkills(resolveSkillsForExtended(createStudentExtendedReq));

        try {
            studentEnt = studentRepo.save(studentEnt);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Student already exists: {}, {}", createStudentExtendedReq.email(), createStudentExtendedReq.telegramUsername());
            throw new BadRequestException("Student already exists: %s, %s"
                    .formatted(createStudentExtendedReq.email(), createStudentExtendedReq.telegramUsername()));
        }

        createPortfolioForStudent(studentEnt, createStudentExtendedReq.portfolio());
        createExperiencesForStudent(studentEnt, createStudentExtendedReq.experiences());
        createInstitutionsForStudent(studentEnt, createStudentExtendedReq.institutions());

        StudentDTO studentDTO = studentTools.mapToDTO(studentEnt);
        log.info("User: {}, created extended student: {}", securityHelper.getCurrentUsername(), studentDTO);
        return studentDTO;
    }

    @Override
    @Transactional
    public StudentDTO update(
            UUID id,
            UpdateStudentReq updateStudentReq
    ) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        SpecialityEnt specialityEnt = specialityTools.getSpecialityOrThrow(updateStudentReq.specialityId());
        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(updateStudentReq.skillsIds());

        studentMapper.updateEntityFromDto(updateStudentReq, studentEnt);

        if (studentEnt.getContactInformation() == null) {
            studentEnt.setContactInformation(new ContactInformation());
        }

        studentEnt.getUserInformation().setFirstName(updateStudentReq.firstName());
        studentEnt.getUserInformation().setLastName(updateStudentReq.lastName());
        studentEnt.setSpeciality(specialityEnt);
        studentEnt.setSkills(skillEntSet);

        StudentDTO studentDTO = studentTools.mapToDTO(studentEnt);

        log.info("User: {}, updated a student: {} with data: {}", securityHelper.getCurrentUsername(), id, studentDTO);

        return studentDTO;
    }

    @Override
    @Transactional
    public StudentDTO patch(UUID id, PatchStudentReq patchStudentReq) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        if (patchStudentReq.city() != null) {
            studentEnt.setCity(patchStudentReq.city());
        }
        if (patchStudentReq.hhLink() != null) {
            studentEnt.setHhLink(patchStudentReq.hhLink());
        }
        if (patchStudentReq.birthDate() != null) {
            studentEnt.setBirthDate(patchStudentReq.birthDate());
        }
        if (patchStudentReq.bio() != null) {
            studentEnt.setBio(patchStudentReq.bio());
        }
        if (patchStudentReq.course() != null) {
            studentEnt.setCourse(patchStudentReq.course());
        }
        if (patchStudentReq.busyness() != null) {
            studentEnt.setBusyness(patchStudentReq.busyness());
        }

        if (studentEnt.getContactInformation() == null) {
            studentEnt.setContactInformation(new ContactInformation());
        }

        if (patchStudentReq.firstName() != null) {
            studentEnt.getUserInformation().setFirstName(patchStudentReq.firstName());
        }
        if (patchStudentReq.lastName() != null) {
            studentEnt.getUserInformation().setLastName(patchStudentReq.lastName());
        }
        if (patchStudentReq.email() != null) {
            studentEnt.getUserInformation().setEmail(patchStudentReq.email());
        }
        if (patchStudentReq.phoneNumber() != null) {
            studentEnt.getContactInformation().setPhoneNumber(patchStudentReq.phoneNumber());
        }
        if (patchStudentReq.telegramUsername() != null) {
            studentEnt.getContactInformation().setTelegramUsername(patchStudentReq.telegramUsername());
        }

        if (patchStudentReq.specialityId() != null) {
            studentEnt.setSpeciality(specialityTools.getSpecialityOrThrow(patchStudentReq.specialityId()));
        }
        if (patchStudentReq.skillsIds() != null) {
            studentEnt.setSkills(resolveSkillsByIdsOrThrow(patchStudentReq.skillsIds()));
        }

        StudentDTO studentDTO = studentTools.mapToDTO(studentEnt);
        log.info("User: {}, patched student: {} with data: {}", securityHelper.getCurrentUsername(), id, studentDTO);
        return studentDTO;
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        StudentEnt studentEnt = studentTools.getStudentOrThrow(id);

        try {
            studentRepo.delete(studentEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting student: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting student");
        }

        log.info("User: {}, deleted a student: {} with data: {}", securityHelper.getCurrentUsername(), id, studentTools.mapToDTO(studentEnt));
    }

    private AddStudentReq toAddStudentReq(CreateStudentExtendedReq req) {
        return new AddStudentReq(
                req.city(),
                req.hhLink(),
                req.birthDate(),
                req.bio(),
                req.course(),
                req.busyness(),
                req.firstName(),
                req.lastName(),
                req.email(),
                req.phoneNumber(),
                req.telegramUsername(),
                req.specialityId(),
                List.of()
        );
    }

    private Set<SkillEnt> resolveSkillsByIdsOrThrow(List<Long> skillsIds) {
        if (skillsIds == null || skillsIds.isEmpty()) {
            return Set.of();
        }

        Set<Long> uniqueSkillIds = new HashSet<>(skillsIds);
        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(uniqueSkillIds);
        if (skillEntSet.size() != uniqueSkillIds.size()) {
            throw new BadRequestException("Some skills were not found by ids");
        }
        return skillEntSet;
    }

    private Set<SkillEnt> resolveSkillsForExtended(CreateStudentExtendedReq req) {
        Set<SkillEnt> resolvedSkills = new HashSet<>(resolveSkillsByIdsOrThrow(req.skillsIds()));

        if (req.skills() == null || req.skills().isEmpty()) {
            return resolvedSkills;
        }

        for (CreateStudentSkillReq skillReq : req.skills()) {
            if (skillReq == null) {
                continue;
            }

            if (skillReq.id() != null) {
                resolvedSkills.add(skillTools.getSkillOrThrow(skillReq.id()));
                continue;
            }

            if (hasText(skillReq.name())) {
                throw new BadRequestException("Skill id or skill name is required");
            }

            String skillName = normalize(skillReq.name());
            SkillEnt skillEnt = skillRepo.findByNameIgnoreCase(skillName)
                    .orElseGet(() -> createSkill(skillName));
            resolvedSkills.add(skillEnt);
        }

        return resolvedSkills;
    }

    private SkillEnt createSkill(String skillName) {
        try {
            return skillRepo.save(new SkillEnt(skillName));
        } catch (DataIntegrityViolationException ignored) {
            return skillRepo.findByNameIgnoreCase(skillName)
                    .orElseThrow(() -> new BadRequestException("Unable to create skill: " + skillName));
        }
    }

    private void createPortfolioForStudent(StudentEnt studentEnt, List<CreateStudentPortfolioReq> portfolioItems) {
        if (portfolioItems == null || portfolioItems.isEmpty()) {
            return;
        }

        for (CreateStudentPortfolioReq item : portfolioItems) {
            if (item == null) {
                continue;
            }

            PortfolioEnt portfolioEnt = new PortfolioEnt();
            portfolioEnt.setName(item.name());
            portfolioEnt.setLink(item.link());
            portfolioEnt.setAdditionalInfo(item.additionalInfo());
            portfolioEnt.setStudent(studentEnt);

            try {
                portfolioRepo.save(portfolioEnt);
            } catch (DataIntegrityViolationException ex) {
                log.warn("Portfolio already exists: {}", item.name());
                throw new BadRequestException("Portfolio already exists: " + item.name());
            }
        }
    }

    private void createExperiencesForStudent(StudentEnt studentEnt, List<CreateStudentExperienceReq> experienceItems) {
        if (experienceItems == null || experienceItems.isEmpty()) {
            return;
        }

        for (CreateStudentExperienceReq item : experienceItems) {
            if (item == null) {
                continue;
            }
            if (item.startDate() == null) {
                throw new BadRequestException("Experience startDate is required");
            }

            CompanyEnt companyEnt = resolveCompany(item);

            ExperienceEnt experienceEnt = new ExperienceEnt();
            experienceEnt.setCompany(companyEnt);
            experienceEnt.setStudent(studentEnt);
            experienceEnt.setPosition(item.position());
            experienceEnt.setAdditionalInfo(item.additionalInfo());
            experienceEnt.setStartDate(item.startDate());
            experienceEnt.setEndDate(item.endDate());

            try {
                experienceRepo.save(experienceEnt);
            } catch (DataIntegrityViolationException ex) {
                log.warn("Experience already exists for student {}", studentEnt.getId());
                throw new BadRequestException("Experience already exists");
            }
        }
    }

    private CompanyEnt resolveCompany(CreateStudentExperienceReq item) {
        if (item.companyId() != null) {
            return companyRepo.findById(item.companyId())
                    .orElseThrow(() -> new BadRequestException("Company not found: " + item.companyId()));
        }
        if (hasText(item.companyName())) {
            throw new BadRequestException("Experience companyId or companyName is required");
        }

        String companyName = normalize(item.companyName());
        return companyRepo.findFirstByNameIgnoreCase(companyName)
                .orElseGet(() -> companyRepo.save(new CompanyEnt(companyName)));
    }

    private void createInstitutionsForStudent(StudentEnt studentEnt, List<CreateStudentInstitutionReq> institutionItems) {
        if (institutionItems == null || institutionItems.isEmpty()) {
            return;
        }

        for (CreateStudentInstitutionReq item : institutionItems) {
            if (item == null) {
                continue;
            }

            EducationEnt educationEnt = resolveEducation(item);

            InstitutionEnt institutionEnt = new InstitutionEnt();
            institutionEnt.setEducation(educationEnt);
            institutionEnt.setStudent(studentEnt);
            institutionEnt.setStartYear(item.startYear());
            institutionEnt.setEndYear(item.endYear());

            try {
                institutionRepo.save(institutionEnt);
            } catch (DataIntegrityViolationException ex) {
                log.warn("Institution already exists for student {}", studentEnt.getId());
                throw new BadRequestException("Institution already exists");
            }
        }
    }

    private EducationEnt resolveEducation(CreateStudentInstitutionReq item) {
        if (item.educationId() != null) {
            return educationRepo.findById(item.educationId())
                    .orElseThrow(() -> new BadRequestException("Education not found: " + item.educationId()));
        }

        if (hasText(item.institution())) {
            throw new BadRequestException("Institution educationId or institution name is required");
        }
        if (hasText(item.webUrl())) {
            throw new BadRequestException("Institution webUrl is required for new education");
        }

        String institutionName = normalize(item.institution());
        EducationEnt existing = educationRepo.findFirstByInstitutionIgnoreCase(institutionName).orElse(null);
        if (existing != null) {
            return existing;
        }

        EducationEnt educationEnt = new EducationEnt();
        educationEnt.setInstitution(institutionName);
        educationEnt.setAdditionalInfo(item.additionalInfo());
        educationEnt.setWebUrl(normalize(item.webUrl()));
        return educationRepo.save(educationEnt);
    }

    private String normalize(String value) {
        return value == null ? null : value.trim();
    }

    private boolean hasText(String value) {
        return value == null || Objects.equals(value.trim(), "");
    }
}
