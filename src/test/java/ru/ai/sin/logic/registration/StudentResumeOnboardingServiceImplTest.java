package ru.ai.sin.logic.registration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ai.sin.logic.experience.ExperienceRepo;
import ru.ai.sin.logic.institution.InstitutionRepo;
import ru.ai.sin.logic.portfolio.PortfolioMapper;
import ru.ai.sin.logic.portfolio.PortfolioRepo;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.speciality.SpecialityEnt;
import ru.ai.sin.logic.student.StudentCvAttachmentService;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.student.StudentService;
import ru.ai.sin.logic.student.StudentSkillsMutator;
import ru.ai.sin.logic.student.dto.StudentDTO;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.embeddables.UserInformation;
import ru.ai.sin.models.enums.BusynessEnum;
import ru.ai.sin.models.enums.CourseEnum;
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.tools.SpecialityTools;
import ru.ai.sin.tools.UserTools;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentResumeOnboardingServiceImplTest {

    private static final UUID STUDENT_ID = UUID.fromString("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa");

    @Mock
    private StudentService studentService;
    @Mock
    private UserRepo userRepo;
    @Mock
    private StudentRepo studentRepo;
    @Mock
    private UserTools userTools;
    @Mock
    private StudentCvAttachmentService studentCvAttachmentService;
    @Mock
    private ExperienceRepo experienceRepo;
    @Mock
    private InstitutionRepo institutionRepo;
    @Mock
    private StudentSkillsMutator studentSkillsMutator;
    @Mock
    private SpecialityTools specialityTools;
    @Mock
    private PortfolioRepo portfolioRepo;
    @Mock
    private PortfolioMapper portfolioMapper;

    private StudentResumeOnboardingServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new StudentResumeOnboardingServiceImpl(
                studentService,
                userRepo,
                studentRepo,
                userTools,
                studentCvAttachmentService,
                experienceRepo,
                institutionRepo,
                studentSkillsMutator,
                specialityTools,
                portfolioRepo,
                portfolioMapper
        );
    }

    @Test
    void hasResumeForCurrentUser_falseForRegistrationDraft() {
        UserEnt user = studentUser(draftStudent());
        when(userTools.findCurrentUserFetchingLinks()).thenReturn(Optional.of(user));

        assertThat(service.hasResumeForCurrentUser()).isFalse();
    }

    @Test
    void hasResumeForCurrentUser_trueWhenResumeComplete() {
        StudentEnt student = draftStudent();
        student.setCourse(CourseEnum.THIRD);
        student.setBirthDate(LocalDate.of(2000, 1, 1));
        SpecialityEnt speciality = new SpecialityEnt();
        speciality.setId(5L);
        student.setSpeciality(speciality);
        student.getUserInformation().setFirstName("Иван");
        student.getUserInformation().setLastName("Иванов");

        UserEnt user = studentUser(student);
        when(userTools.findCurrentUserFetchingLinks()).thenReturn(Optional.of(user));

        assertThat(service.hasResumeForCurrentUser()).isTrue();
    }

    @Test
    void completeResume_updatesExistingDraftWithoutCreateExtended() {
        StudentEnt student = draftStudent();
        UserEnt user = studentUser(student);
        when(userTools.findCurrentUserFetchingLinks()).thenReturn(Optional.of(user));

        SpecialityEnt speciality = new SpecialityEnt();
        speciality.setId(3L);
        when(specialityTools.getSpecialityOrThrow(3L)).thenReturn(speciality);

        StudentDTO updated = new StudentDTO(
                STUDENT_ID, "Москва", null, LocalDate.of(2000, 1, 1), null, null,
                CourseEnum.FIRST, BusynessEnum.FREE, "Иван", "Иванов", "a@b.c", null, null,
                3L, "S", List.of(), false, false, 0, null);
        when(studentService.getLinkedForCurrentUser()).thenReturn(Optional.of(updated));

        StudentResumeOnboardingReq req = onboardingReq();
        service.completeResume(req);

        verify(studentService, never()).createExtended(any());
        verify(studentSkillsMutator).replaceSkills(student, List.of());
    }

    private static UserEnt studentUser(StudentEnt student) {
        UserEnt user = new UserEnt(RoleEnum.STUDENT, "Иван Иванов", "student1", "hash");
        user.setStudent(student);
        return user;
    }

    private static StudentEnt draftStudent() {
        StudentEnt student = new StudentEnt();
        student.setId(STUDENT_ID);
        student.setCatalogVisible(false);
        student.setUserInformation(new UserInformation());
        return student;
    }

    private static StudentResumeOnboardingReq onboardingReq() {
        return new StudentResumeOnboardingReq(
                "Москва",
                null,
                LocalDate.of(2000, 1, 1),
                null,
                CourseEnum.FIRST,
                BusynessEnum.FREE,
                "Иван",
                "Иванов",
                "a@b.c",
                "+79990001122",
                null,
                3L,
                List.of(),
                List.of(),
                List.of()
        );
    }
}
