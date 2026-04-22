package ru.ai.sin.logic.recruiter.registration;

import jakarta.servlet.http.HttpServletRequest;
import ru.ai.sin.logic.recruiter.registration.dto.RecruiterSelfRegistrationReq;

public interface RecruiterSelfRegistrationService {

    /**
     * Создаёт заявку PENDING. Аккаунт в users не создаётся — после одобрения админом пользователь сможет войти.
     */
    void submit(RecruiterSelfRegistrationReq req, HttpServletRequest httpRequest);
}
