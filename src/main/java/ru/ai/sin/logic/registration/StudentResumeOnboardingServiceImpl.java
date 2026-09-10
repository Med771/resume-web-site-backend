package ru.ai.sin.logic.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.logic.experience.ExperienceEnt;
import ru.ai.sin.logic.experience.ExperienceRepo;
import ru.ai.sin.logic.institution.InstitutionEnt;
import ru.ai.sin.logic.institution.InstitutionRepo;
import ru.ai.sin.logic.portfolio.PortfolioEnt;
import ru.ai.sin.logic.portfolio.PortfolioMapper;
import ru.ai.sin.logic.portfolio.PortfolioRepo;
import ru.ai.sin.logic.portfolio.dto.AddPortfolioReq;
import ru.ai.sin.logic.portfolio.dto.PortfolioDTO;
import ru.ai.sin.logic.registration.dto.StudentPortfolioItemReq;
import ru.ai.sin.logic.registration.dto.StudentResumeEditRes;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.skill.SkillEnt;
import ru.ai.sin.logic.student.StudentCvAttachmentService;
import ru.ai.sin.logic.student.StudentSkillsMutator;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentProfileScoring;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.student.StudentService;
import ru.ai.sin.logic.student.dto.CreateStudentExtendedReq;
import ru.ai.sin.logic.student.dto.StudentDTO;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.embeddables.ContactInformation;
import ru.ai.sin.models.embeddables.UserInformation;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.models.enums.CourseEnum;
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.tools.SpecialityTools;
import ru.ai.sin.tools.UserTools;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentResumeOnboardingServiceImpl implements StudentResumeOnboardingService {

    private final StudentService studentService;
    private final UserRepo userRepo;
    private final StudentRepo studentRepo;
    private final UserTools userTools;
    private final StudentCvAttachmentService studentCvAttachmentService;
    private final ExperienceRepo experienceRepo;
    private final InstitutionRepo institutionRepo;
    private final StudentSkillsMutator studentSkillsMutator;
    private final SpecialityTools specialityTools;
    private final PortfolioRepo portfolioRepo;
    private final PortfolioMapper portfolioMapper;

    @Override
    @Transactional
    public StudentDTO completeResume(StudentResumeOnboardingReq req) {
        UserEnt user = requireStudentUser();
        validateCourse(req.course());
        if (user.getStudent() != null) {
            return updateResume(req);
        }

        CreateStudentExtendedReq extendedReq = toExtendedReq(req, req.course());
        StudentDTO created = studentService.createExtended(extendedReq);
        studentRepo.findById(created.id()).ifPresent(student -> {
            student.setCatalogVisible(false);
            studentRepo.save(student);
            user.setStudent(student);
            syncRegistrationContacts(user, req);
            userRepo.save(user);
        });

        log.info("Student resume onboarding completed: username={} studentId={}", user.getUsername(), created.id());
        return created;
    }

    @Override
    @Transactional
    public StudentDTO updateResume(StudentResumeOnboardingReq req) {
        UserEnt user = requireStudentUser();
        validateCourse(req.course());
        StudentEnt studentEnt = user.getStudent();
        if (studentEnt == null) {
            throw new BadRequestException("К аккаунту не привязана карточка студента");
        }

        applyProfileFields(studentEnt, req);
        studentEnt.setSpeciality(specialityTools.getSpecialityOrThrow(req.specialityId()));
        studentSkillsMutator.replaceSkills(studentEnt, req.skillsIds());

        studentCvAttachmentService.attachExperiences(studentEnt, req.experiences());
        studentCvAttachmentService.attachInstitutions(studentEnt, req.institutions());

        StudentProfileScoring.applyTo(studentEnt);
        syncRegistrationContacts(user, req);

        StudentDTO updated = studentService.getLinkedForCurrentUser()
                .orElseThrow(() -> new BadRequestException("Не удалось обновить резюме"));
        log.info("Student resume updated: username={} studentId={}", user.getUsername(), studentEnt.getId());
        return updated;
    }

    @Override
    @Transactional(readOnly = true)
    public StudentResumeEditRes getResumeForEdit() {
        UserEnt user = requireStudentUser();
        StudentEnt studentEnt = user.getStudent();
        if (studentEnt == null) {
            return buildResumeDefaultsFromUser(user);
        }

        List<Long> skillsIds = studentRepo.findSkillsByStudentId(studentEnt.getId()).stream()
                .map(SkillEnt::getId)
                .toList();

        List<StudentResumeEditRes.StudentResumeExperienceItem> experiences = experienceRepo
                .findAllByStudent_Id(studentEnt.getId()).stream()
                .map(this::toExperienceItem)
                .toList();

        List<StudentResumeEditRes.StudentResumeInstitutionItem> institutions = institutionRepo
                .findAllByStudent_Id(studentEnt.getId()).stream()
                .map(this::toInstitutionItem)
                .toList();

        var contact = studentEnt.getContactInformation();
        var userInfo = studentEnt.getUserInformation();
        NameParts nameParts = parseDisplayName(user.getName());

        return new StudentResumeEditRes(
                studentEnt.getCity(),
                studentEnt.getHhLink(),
                studentEnt.getBirthDate(),
                studentEnt.getBio(),
                studentEnt.getBusyness(),
                studentEnt.getCourse(),
                coalesce(userInfo != null ? userInfo.getFirstName() : null, nameParts.firstName()),
                coalesce(userInfo != null ? userInfo.getLastName() : null, nameParts.lastName()),
                coalesce(userInfo != null ? userInfo.getEmail() : null, user.getRegistrationEmail()),
                coalesce(contact != null ? contact.getPhoneNumber() : null, user.getRegistrationPhone()),
                contact != null ? contact.getTelegramUsername() : null,
                studentEnt.getSpeciality() != null ? studentEnt.getSpeciality().getId() : 0L,
                skillsIds,
                experiences,
                institutions
        );
    }

    private StudentResumeEditRes buildResumeDefaultsFromUser(UserEnt user) {
        NameParts nameParts = parseDisplayName(user.getName());
        return new StudentResumeEditRes(
                null,
                null,
                null,
                null,
                BusynessEnum.FREE,
                null,
                nameParts.firstName(),
                nameParts.lastName(),
                user.getRegistrationEmail(),
                user.getRegistrationPhone(),
                null,
                0L,
                List.of(),
                List.of(),
                List.of()
        );
    }

    private static String coalesce(String primary, String fallback) {
        if (primary != null && !primary.isBlank()) {
            return primary.trim();
        }
        if (fallback != null && !fallback.isBlank()) {
            return fallback.trim();
        }
        return null;
    }

    private static NameParts parseDisplayName(String displayName) {
        if (displayName == null || displayName.isBlank()) {
            return new NameParts(null, null);
        }
        String[] parts = displayName.trim().split("\\s+");
        if (parts.length == 1) {
            return new NameParts(parts[0], null);
        }
        return new NameParts(parts[1], parts[0]);
    }

    private record NameParts(String firstName, String lastName) {}

    @Override
    @Transactional(readOnly = true)
    public boolean hasResumeForCurrentUser() {
        return userTools.findCurrentUserFetchingLinks()
                .filter(u -> u.getRole() == RoleEnum.STUDENT)
                .map(this::isResumeComplete)
                .orElse(false);
    }

    private boolean isResumeComplete(UserEnt user) {
        StudentEnt student = user.getStudent();
        if (student == null) {
            return false;
        }
        UserInformation userInfo = student.getUserInformation();
        return student.getCourse() != null
                && student.getBirthDate() != null
                && student.getSpeciality() != null
                && userInfo != null
                && isNotBlank(userInfo.getFirstName())
                && isNotBlank(userInfo.getLastName());
    }

    private static boolean isNotBlank(String value) {
        return value != null && !value.isBlank();
    }

    private UserEnt requireStudentUser() {
        UserEnt user = userTools.findCurrentUserFetchingLinks()
                .orElseThrow(() -> new BadRequestException("Требуется вход в систему"));
        if (user.getRole() != RoleEnum.STUDENT) {
            throw new BadRequestException("Доступно только студентам");
        }
        return user;
    }

    private CreateStudentExtendedReq toExtendedReq(StudentResumeOnboardingReq req, CourseEnum course) {
        return new CreateStudentExtendedReq(
                req.city(),
                req.hhLink(),
                req.birthDate(),
                req.bio(),
                course,
                req.busyness(),
                req.firstName(),
                req.lastName(),
                req.email(),
                req.phoneNumber(),
                req.telegramUsername(),
                req.specialityId(),
                req.skillsIds() != null ? req.skillsIds() : List.of(),
                List.of(),
                List.of(),
                req.experiences(),
                req.institutions(),
                null,
                null,
                false,
                null
        );
    }

    private void applyProfileFields(StudentEnt studentEnt, StudentResumeOnboardingReq req) {
        studentEnt.setCity(req.city());
        studentEnt.setHhLink(req.hhLink());
        studentEnt.setBirthDate(req.birthDate());
        studentEnt.setBio(req.bio());
        studentEnt.setBusyness(req.busyness());
        studentEnt.setCourse(req.course());

        if (studentEnt.getUserInformation() == null) {
            studentEnt.setUserInformation(new UserInformation());
        }
        if (studentEnt.getContactInformation() == null) {
            studentEnt.setContactInformation(new ContactInformation());
        }

        studentEnt.getUserInformation().setFirstName(req.firstName());
        studentEnt.getUserInformation().setLastName(req.lastName());
        studentEnt.getUserInformation().setEmail(req.email());
        studentEnt.getContactInformation().setPhoneNumber(req.phoneNumber());
        studentEnt.getContactInformation().setTelegramUsername(req.telegramUsername());
    }

    private static void validateCourse(CourseEnum course) {
        if (course == null) {
            throw new BadRequestException("Укажите курс обучения (1–4)");
        }
    }

    private void syncRegistrationContacts(UserEnt user, StudentResumeOnboardingReq req) {
        if (req.email() != null && !req.email().isBlank()) {
            user.setRegistrationEmail(req.email().trim());
        }
        if (req.phoneNumber() != null && !req.phoneNumber().isBlank()) {
            user.setRegistrationPhone(req.phoneNumber().trim());
        }
        userRepo.save(user);
    }

    private StudentResumeEditRes.StudentResumeExperienceItem toExperienceItem(ExperienceEnt ent) {
        String companyName = ent.getCompany() != null ? ent.getCompany().getName() : null;
        return new StudentResumeEditRes.StudentResumeExperienceItem(
                ent.getId(),
                companyName,
                ent.getPosition(),
                ent.getAdditionalInfo(),
                ent.getStartDate(),
                ent.getEndDate()
        );
    }

    private StudentResumeEditRes.StudentResumeInstitutionItem toInstitutionItem(InstitutionEnt ent) {
        var education = ent.getEducation();
        return new StudentResumeEditRes.StudentResumeInstitutionItem(
                ent.getId(),
                education != null ? education.getInstitution() : null,
                education != null ? education.getWebUrl() : null,
                education != null ? education.getAdditionalInfo() : null,
                ent.getStartYear(),
                ent.getEndYear()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<PortfolioDTO> listPortfoliosForCurrentUser() {
        StudentEnt student = requireStudentWithProfile();
        return portfolioRepo.findAllByStudent_Id(student.getId()).stream()
                .map(portfolioMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public PortfolioDTO addPortfolio(StudentPortfolioItemReq req) {
        StudentEnt student = requireStudentWithProfile();
        AddPortfolioReq addReq = new AddPortfolioReq(
                req.name().trim(),
                req.link().trim(),
                req.additionalInfo() != null && !req.additionalInfo().isBlank() ? req.additionalInfo().trim() : null,
                student.getId());
        PortfolioEnt entity = portfolioMapper.toEntity(addReq, student);
        return portfolioMapper.toDTO(portfolioRepo.save(entity));
    }

    @Override
    @Transactional
    public void deletePortfolio(long portfolioId) {
        StudentEnt student = requireStudentWithProfile();
        PortfolioEnt portfolio = portfolioRepo.findById(portfolioId)
                .orElseThrow(() -> new BadRequestException("Портфолио не найдено"));
        if (!portfolio.getStudent().getId().equals(student.getId())) {
            throw new BadRequestException("Нет доступа к этой записи портфолио");
        }
        portfolioRepo.delete(portfolio);
    }

    private StudentEnt requireStudentWithProfile() {
        UserEnt user = requireStudentUser();
        if (user.getStudent() == null) {
            throw new BadRequestException("К аккаунту не привязана карточка студента");
        }
        return user.getStudent();
    }
}
