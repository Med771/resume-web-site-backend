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
import ru.ai.sin.logic.registration.dto.StudentResumeEditRes;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.skill.SkillEnt;
import ru.ai.sin.logic.skill.SkillRepo;
import ru.ai.sin.logic.student.StudentCvAttachmentService;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentProfileScoring;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.student.StudentService;
import ru.ai.sin.logic.student.dto.CreateStudentExtendedReq;
import ru.ai.sin.logic.student.dto.StudentDTO;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.enums.CourseEnum;
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.tools.SpecialityTools;
import ru.ai.sin.tools.UserTools;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final SkillRepo skillRepo;
    private final SpecialityTools specialityTools;

    @Override
    @Transactional
    public StudentDTO completeResume(StudentResumeOnboardingReq req) {
        UserEnt user = requireStudentUser();
        if (user.getStudent() != null) {
            throw new BadRequestException("Резюме уже создано");
        }

        CreateStudentExtendedReq extendedReq = toExtendedReq(req, CourseEnum.NEW);
        StudentDTO created = studentService.createExtended(extendedReq);
        studentRepo.findById(created.id()).ifPresent(student -> {
            user.setStudent(student);
            userRepo.save(user);
        });

        log.info("Student resume onboarding completed: username={} studentId={}", user.getUsername(), created.id());
        return created;
    }

    @Override
    @Transactional
    public StudentDTO updateResume(StudentResumeOnboardingReq req) {
        UserEnt user = requireStudentUser();
        StudentEnt studentEnt = user.getStudent();
        if (studentEnt == null) {
            throw new BadRequestException("К аккаунту не привязана карточка студента");
        }

        applyProfileFields(studentEnt, req);
        studentEnt.setSpeciality(specialityTools.getSpecialityOrThrow(req.specialityId()));
        studentEnt.setSkills(resolveSkills(req.skillsIds()));

        studentCvAttachmentService.attachExperiences(studentEnt, req.experiences());
        studentCvAttachmentService.attachInstitutions(studentEnt, req.institutions());

        StudentProfileScoring.applyTo(studentEnt);
        studentRepo.save(studentEnt);

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
            throw new BadRequestException("К аккаунту не привязана карточка студента");
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

        return new StudentResumeEditRes(
                studentEnt.getCity(),
                studentEnt.getHhLink(),
                studentEnt.getBirthDate(),
                studentEnt.getBio(),
                studentEnt.getBusyness(),
                userInfo != null ? userInfo.getFirstName() : null,
                userInfo != null ? userInfo.getLastName() : null,
                userInfo != null ? userInfo.getEmail() : null,
                contact != null ? contact.getPhoneNumber() : null,
                contact != null ? contact.getTelegramUsername() : null,
                studentEnt.getSpeciality().getId(),
                skillsIds,
                experiences,
                institutions
        );
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasResumeForCurrentUser() {
        return userTools.findCurrentUserFetchingLinks()
                .filter(u -> u.getRole() == RoleEnum.STUDENT)
                .map(u -> u.getStudent() != null)
                .orElse(false);
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

        if (studentEnt.getUserInformation() != null) {
            studentEnt.getUserInformation().setFirstName(req.firstName());
            studentEnt.getUserInformation().setLastName(req.lastName());
            studentEnt.getUserInformation().setEmail(req.email());
        }
        if (studentEnt.getContactInformation() != null) {
            studentEnt.getContactInformation().setPhoneNumber(req.phoneNumber());
            studentEnt.getContactInformation().setTelegramUsername(req.telegramUsername());
        }
    }

    private Set<SkillEnt> resolveSkills(List<Long> skillsIds) {
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
}
