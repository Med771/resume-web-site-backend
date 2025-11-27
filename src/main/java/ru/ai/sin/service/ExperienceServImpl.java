package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.experience.AddExperienceReq;
import ru.ai.sin.dto.experience.ExperienceDTO;
import ru.ai.sin.dto.experience.GetAboutCompanyRes;
import ru.ai.sin.dto.experience.GetAboutStudentRes;
import ru.ai.sin.repository.ExperienceRepo;
import ru.ai.sin.service.impl.ExperienceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceServImpl implements ExperienceService {

    private final ExperienceRepo experienceRepo;

    @Override
    public ExperienceDTO getById(long id) {
        return null;
    }

    @Override
    public GetAboutCompanyRes getAboutCompanyById(long id, long page, long size) {
        return null;
    }

    @Override
    public GetAboutStudentRes getAboutStudentById(UUID id, long page, long size) {
        return null;
    }

    @Override
    public List<ExperienceDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public ExperienceDTO create(AddExperienceReq addExperienceReq) {
        return null;
    }

    @Override
    public ExperienceDTO update(long id, AddExperienceReq addExperienceReq) {
        return null;
    }

    @Override
    public ExperienceDTO deleteById(long id) {
        return null;
    }
}
