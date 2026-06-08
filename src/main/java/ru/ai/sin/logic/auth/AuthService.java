package ru.ai.sin.logic.auth;

import jakarta.servlet.http.HttpServletRequest;
import ru.ai.sin.logic.auth.dto.AuthMeDTO;
import ru.ai.sin.logic.auth.dto.ChangePasswordReq;
import ru.ai.sin.logic.auth.dto.LoginRequest;
import ru.ai.sin.logic.auth.dto.TokenPair;

public interface AuthService {

    TokenPair login(LoginRequest request);

    String refresh(HttpServletRequest request);

    AuthMeDTO getCurrentSession();

    void changePassword(ChangePasswordReq req);
}
