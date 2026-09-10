package ru.ai.sin.logic.registration;

import jakarta.servlet.http.HttpServletRequest;
import ru.ai.sin.logic.auth.dto.TokenPair;
import ru.ai.sin.logic.registration.dto.StudentAccountRegistrationReq;

public interface StudentRegistrationService {

    /**
     * Создаёт пользователя STUDENT и черновик карточки ({@code catalogVisible=false}), выполняет аутентификацию.
     * Дозаполнение резюме — {@code POST /student/onboarding/resume}.
     */
    TokenPair registerAndIssueTokens(StudentAccountRegistrationReq req, HttpServletRequest httpRequest);
}
