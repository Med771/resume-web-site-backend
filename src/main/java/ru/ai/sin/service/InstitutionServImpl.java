package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.institution.AddInstitutionReq;
import ru.ai.sin.dto.institution.GetAboutEducationRes;
import ru.ai.sin.dto.institution.GetAboutStudentRes;
import ru.ai.sin.dto.institution.InstitutionDTO;
import ru.ai.sin.repository.InstitutionRepo;
import ru.ai.sin.service.impl.InstitutionService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionServImpl implements InstitutionService {

    private final InstitutionRepo  institutionRepo;

    @Override
    public InstitutionDTO getById(long id) {
        return null;
    }

    @Override
    public GetAboutEducationRes getByEducationId(long id) {
        return null;
    }

    @Override
    public GetAboutStudentRes getByStudentId(UUID id) {
        return null;
    }

    @Override
    public List<InstitutionDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public InstitutionDTO create(AddInstitutionReq addInstitutionReq) {
        return null;
    }

    @Override
    public InstitutionDTO update(long id, AddInstitutionReq addInstitutionReq) {
        return null;
    }

    @Override
    public InstitutionDTO deleteById(long id) {
        return null;
    }
}
