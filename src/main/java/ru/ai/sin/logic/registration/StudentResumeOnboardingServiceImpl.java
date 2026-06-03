package ru.ai.sin.logic.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.student.StudentService;
import ru.ai.sin.logic.student.dto.CreateStudentExtendedReq;
import ru.ai.sin.logic.student.dto.StudentDTO;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.enums.CourseEnum;
import ru.ai.sin.models.enums.RoleEnum;
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

    @Override
    @Transactional
    public StudentDTO completeResume(StudentResumeOnboardingReq req) {
        UserEnt user = userTools.findCurrentUserFetchingLinks()
                .orElseThrow(() -> new BadRequestException("Требуется вход в систему"));
        if (user.getRole() != RoleEnum.STUDENT) {
            throw new BadRequestException("Онбординг резюме доступен только студентам");
        }
        if (user.getStudent() != null) {
            throw new BadRequestException("Резюме уже создано");
        }

        CreateStudentExtendedReq extendedReq = new CreateStudentExtendedReq(
                req.city(),
                req.hhLink(),
                req.birthDate(),
                req.bio(),
                CourseEnum.NEW,
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

        StudentDTO created = studentService.createExtended(extendedReq);
        studentRepo.findById(created.id()).ifPresent(student -> {
            user.setStudent(student);
            userRepo.save(user);
        });

        log.info("Student resume onboarding completed: username={} studentId={}", user.getUsername(), created.id());
        return created;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasResumeForCurrentUser() {
        return userTools.findCurrentUserFetchingLinks()
                .filter(u -> u.getRole() == RoleEnum.STUDENT)
                .map(u -> u.getStudent() != null)
                .orElse(false);
    }
}
