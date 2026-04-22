package ru.ai.sin.logic.registration;

import jakarta.servlet.http.HttpServletRequest;
import ru.ai.sin.logic.auth.dto.TokenPair;
import ru.ai.sin.logic.registration.dto.StudentSelfRegistrationReq;

public interface StudentRegistrationService {

    /**
     * Создаёт студента + пользователя STUDENT, выполняет аутентификацию и возвращает пару JWT для выдачи в cookie.
     */
    TokenPair registerAndIssueTokens(StudentSelfRegistrationReq req, HttpServletRequest httpRequest);
}
