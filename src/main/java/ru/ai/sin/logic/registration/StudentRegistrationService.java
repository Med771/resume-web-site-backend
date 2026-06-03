package ru.ai.sin.logic.registration;

import jakarta.servlet.http.HttpServletRequest;
import ru.ai.sin.logic.auth.dto.TokenPair;
import ru.ai.sin.logic.registration.dto.StudentAccountRegistrationReq;

public interface StudentRegistrationService {

    /**
     * Создаёт пользователя STUDENT (без карточки резюме), выполняет аутентификацию.
     * Резюме — {@code POST /student/onboarding/resume}.
     */
    TokenPair registerAndIssueTokens(StudentAccountRegistrationReq req, HttpServletRequest httpRequest);
}
