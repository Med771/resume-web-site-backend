package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.education.*;
import ru.ai.sin.repository.EducationRepo;
import ru.ai.sin.service.impl.EducationService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationServImpl implements EducationService {

    private final EducationRepo educationRepo;

    @Override
    public EducationDTO getById(long id) {
        return null;
    }

    @Override
    public List<EducationDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public EducationDTO create(AddEducationReq addEducationReq) {
        return null;
    }

    @Override
    public EducationDTO setInstitutionById(long id, SetInstitutionReq setInstitutionReq) {
        return null;
    }

    @Override
    public EducationDTO setAdditionalInfoById(long id, SetEducationInfoReq setEducationInfoReq) {
        return null;
    }

    @Override
    public EducationDTO setSkillsById(long id, SetEducationSkillsReq setEducationSkillsReq) {
        return null;
    }

    @Override
    public EducationDTO deleteById(long id) {
        return null;
    }
}
