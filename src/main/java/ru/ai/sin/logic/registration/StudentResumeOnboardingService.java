package ru.ai.sin.logic.registration;

import ru.ai.sin.logic.portfolio.dto.PortfolioDTO;
import ru.ai.sin.logic.registration.dto.StudentPortfolioItemReq;
import ru.ai.sin.logic.registration.dto.StudentResumeEditRes;
import ru.ai.sin.logic.registration.dto.StudentResumeOnboardingReq;
import ru.ai.sin.logic.student.dto.StudentDTO;

import java.util.List;

public interface StudentResumeOnboardingService {

    StudentDTO completeResume(StudentResumeOnboardingReq req);

    StudentDTO updateResume(StudentResumeOnboardingReq req);

    StudentResumeEditRes getResumeForEdit();

    boolean hasResumeForCurrentUser();

    List<PortfolioDTO> listPortfoliosForCurrentUser();

    PortfolioDTO addPortfolio(StudentPortfolioItemReq req);

    void deletePortfolio(long portfolioId);
}
