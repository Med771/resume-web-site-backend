package ru.ai.sin.service.impl;

import jakarta.servlet.http.HttpServletRequest;
import ru.ai.sin.dto.auth.LoginRequest;
import ru.ai.sin.dto.auth.TokenPair;

public interface AuthService {

    TokenPair login(LoginRequest request);

    String refresh(HttpServletRequest request);


}
