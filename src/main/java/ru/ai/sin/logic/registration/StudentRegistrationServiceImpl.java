package ru.ai.sin.logic.registration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.config.property.RegistrationProperties;
import ru.ai.sin.config.property.UserProperties;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.helper.JwtHelper;
import ru.ai.sin.logic.auth.dto.TokenPair;
import ru.ai.sin.logic.registration.dto.StudentSelfRegistrationReq;
import ru.ai.sin.logic.student.dto.CreateStudentExperienceReq;
import ru.ai.sin.logic.student.dto.CreateStudentInstitutionReq;
import ru.ai.sin.logic.skill.SkillEnt;
import ru.ai.sin.logic.skill.SkillRepo;
import ru.ai.sin.logic.student.StudentCvAttachmentService;
import ru.ai.sin.logic.student.StudentEnt;
import ru.ai.sin.logic.student.StudentMapper;
import ru.ai.sin.logic.student.StudentProfileScoring;
import ru.ai.sin.logic.student.StudentRepo;
import ru.ai.sin.logic.student.dto.AddStudentReq;
import ru.ai.sin.logic.user.UserEnt;
import ru.ai.sin.logic.user.UserRepo;
import ru.ai.sin.models.enums.CourseEnum;
import ru.ai.sin.models.enums.RoleEnum;
import ru.ai.sin.tools.SpecialityTools;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentRegistrationServiceImpl implements StudentRegistrationService {

    private final RegistrationIpRateLimiter registrationIpRateLimiter;
    private final RegistrationPasswordPolicy passwordPolicy;
    private final RegistrationProperties registrationProperties;
    private final UserProperties userProperties;

    private final UserRepo userRepo;
    private final StudentRepo studentRepo;
    private final SkillRepo skillRepo;
    private final StudentMapper studentMapper;
    private final SpecialityTools specialityTools;
    private final StudentCvAttachmentService studentCvAttachmentService;

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;

    @Override
    @Transactional
    public TokenPair registerAndIssueTokens(StudentSelfRegistrationReq req, HttpServletRequest httpRequest) {
        registrationIpRateLimiter.check(
                RegistrationIpRateLimiter.RegistrationRateBucket.STUDENT,
                ClientIpResolver.resolve(httpRequest));

        if (!req.password().equals(req.passwordConfirm())) {
            throw new BadRequestException("Пароли не совпадают");
        }
        passwordPolicy.validate(req.password());

        String username = req.username().trim();
        String email = req.email().trim();

        if (userRepo.existsByUsername(username)) {
            log.warn("Student registration: username already exists");
            throw conflict();
        }
        if (studentRepo.existsByNormalizedEmail(email)) {
            log.warn("Student registration: email already used");
            throw conflict();
        }
        if (registrationProperties.isReservedUsername(username)) {
            throw new BadRequestException("Этот логин зарезервирован");
        }
        if (userProperties.getLogins() != null) {
            for (UserProperties.Login login : userProperties.getLogins()) {
                if (login.getUsername() != null
                        && login.getUsername().equalsIgnoreCase(username)) {
                    throw new BadRequestException("Этот логин зарезервирован");
                }
            }
        }

        List<Long> skillIds = req.skillsIds() == null ? List.of() : req.skillsIds();
        if (skillIds.size() > registrationProperties.getMaxSkillsPerProfile()) {
            throw new BadRequestException("Слишком много навыков в анкете");
        }
        List<CreateStudentExperienceReq> experiences = req.experiences() == null ? List.of() : req.experiences();
        if (experiences.size() > registrationProperties.getMaxExperiencesInRegistration()) {
            throw new BadRequestException("Слишком много записей об опыте работы");
        }
        List<CreateStudentInstitutionReq> institutions = req.institutions() == null ? List.of() : req.institutions();
        if (institutions.size() > registrationProperties.getMaxInstitutionsInRegistration()) {
            throw new BadRequestException("Слишком много записей об образовании");
        }
        Set<SkillEnt> skills = resolveSkills(skillIds);

        AddStudentReq profile = new AddStudentReq(
                req.city(),
                req.hhLink(),
                req.birthDate(),
                req.bio(),
                CourseEnum.NEW,
                req.busyness(),
                req.firstName(),
                req.lastName(),
                email,
                req.phoneNumber(),
                req.telegramUsername(),
                req.specialityId(),
                skillIds,
                null,
                null
        );

        StudentEnt student = studentMapper.toEntity(profile);
        student.setCourse(CourseEnum.NEW);
        student.setSpeciality(specialityTools.getSpecialityOrThrow(req.specialityId()));
        student.setSkills(skills);

        try {
            student = studentRepo.save(student);
        } catch (DataIntegrityViolationException ex) {
            log.warn("Student registration data conflict: {}", ex.getMessage());
            throw conflict();
        }

        studentCvAttachmentService.attachExperiences(student, req.experiences());
        studentCvAttachmentService.attachInstitutions(student, req.institutions());

        StudentProfileScoring.applyTo(student);
        studentRepo.save(student);

        UserEnt user = new UserEnt(
                RoleEnum.STUDENT,
                emptyToNull(req.name()),
                username,
                passwordEncoder.encode(req.password())
        );
        user.setStudent(student);

        try {
            userRepo.save(user);
        } catch (DataIntegrityViolationException ex) {
            log.warn("User registration data conflict: {}", ex.getMessage());
            throw conflict();
        }

        studentRepo.flush();
        userRepo.flush();

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, req.password()));
        UserDetails principal = (UserDetails) auth.getPrincipal();
        String access = jwtHelper.generateAccessToken(principal.getUsername());
        String refresh = jwtHelper.generateRefreshToken(principal.getUsername());
        log.info("Student self-registered: username={} studentId={}", username, student.getId());
        return new TokenPair(access, refresh);
    }

    private static BadRequestException conflict() {
        return new BadRequestException(
                "Не удалось завершить регистрацию. Проверьте данные или войдите, если аккаунт уже есть.");
    }

    private static String emptyToNull(String s) {
        if (s == null || s.isBlank()) {
            return null;
        }
        return s.trim();
    }

    private Set<SkillEnt> resolveSkills(List<Long> skillsIds) {
        if (skillsIds == null || skillsIds.isEmpty()) {
            return Set.of();
        }
        Set<Long> unique = new HashSet<>(skillsIds);
        Set<SkillEnt> found = skillRepo.findAllByIdIn(unique);
        if (found.size() != unique.size()) {
            throw new BadRequestException("Указан неизвестный навык");
        }
        return found;
    }
}
