package ru.ai.sin.logic.registration;

import ru.ai.sin.logic.registration.dto.StudentResumeEditRes;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.student.dto.StudentDTO;

public interface StudentResumeOnboardingService {

    StudentDTO completeResume(StudentResumeOnboardingReq req);

    StudentDTO updateResume(StudentResumeOnboardingReq req);

    StudentResumeEditRes getResumeForEdit();

    boolean hasResumeForCurrentUser();
}
