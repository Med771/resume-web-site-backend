package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.institution.*;

import ru.ai.sin.entity.InstitutionEnt;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.mapper.EducationMapper;
import ru.ai.sin.mapper.InstitutionMapper;

import ru.ai.sin.repository.InstitutionRepo;

import ru.ai.sin.service.impl.InstitutionService;
import ru.ai.sin.service.tools.EducationTools;
import ru.ai.sin.service.tools.InstitutionTools;
import ru.ai.sin.service.tools.StudentTools;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionServImpl implements InstitutionService {

    private final InstitutionRepo institutionRepo;

    private final InstitutionMapper institutionMapper;
    private final EducationMapper educationMapper;

    private final InstitutionTools institutionTools;
    private final EducationTools educationTools;
    private final StudentTools studentTools;

    private void updateActiveEducationOrThrow(long educationId, InstitutionEnt institutionEnt) {
        institutionEnt.setEducation(educationTools.getEducationOrThrow(educationId));
    }

    private void updateActiveStudentOrThrow(UUID studentId, InstitutionEnt institutionEnt) {
        institutionEnt.setStudent(studentTools.getStudentOrThrow(studentId));
    }

    @Override
    public InstitutionDTO getById(long id) {
        InstitutionEnt institutionEnt = institutionTools.getInstitutionOrThrow(id);

        return institutionTools.mapToDTO(institutionEnt);
    }

    @Override
    public GetAboutEducationRes getByEducationId(
            long id,
            int pageInstitutionNumber,
            int pageInstitutionSize
    ) {
        List<InstitutionEnt> institutionEntList = institutionRepo
                .findAllByEducationId(
                        id,
                        PageRequest.of(pageInstitutionNumber, pageInstitutionSize))
                .getContent();

        List<GetAboutEducationRes.GetEducationInstitutionRes> getEducationInstitutionResList = institutionEntList.stream()
                .map(institutionEnt -> new GetAboutEducationRes.GetEducationInstitutionRes(
                        institutionEnt.getStudent().getId(),
                        institutionMapper.toRes(institutionEnt)
                )).toList();

        return new GetAboutEducationRes(id, getEducationInstitutionResList);
    }

    @Override
    public GetAboutStudentRes getByStudentId(
            UUID id,
            int pageInstitutionNumber,
            int pageInstitutionSize
    ) {
        List<InstitutionEnt> institutionEntList = institutionRepo
                .findAllByStudentId(
                        id,
                        PageRequest.of(pageInstitutionNumber, pageInstitutionSize))
                .getContent();

        List<GetAboutStudentRes.GetStudentInstitutionRes> getStudentInstitutionResList = institutionEntList.stream()
                .map(institutionEnt -> new GetAboutStudentRes.GetStudentInstitutionRes(
                        educationMapper.toRes(institutionEnt.getEducation()),
                        institutionMapper.toRes(institutionEnt)
                )).toList();

        return new GetAboutStudentRes(id, getStudentInstitutionResList);
    }

    @Override
    public List<InstitutionDTO> getAll(
            int pageInstitutionNumber,
            int pageInstitutionSize
    ) {
        List<InstitutionEnt> institutionEntList = institutionRepo
                .findAll(
                        PageRequest.of(pageInstitutionNumber, pageInstitutionSize))
                .getContent();

        return institutionEntList.stream()
                .map(institutionTools::mapToDTO).toList();
    }

    @Override
    @Transactional
    public InstitutionDTO create(AddInstitutionReq addInstitutionReq) {
        InstitutionEnt institutionEnt = institutionMapper.toEntity(addInstitutionReq);

        updateActiveEducationOrThrow(addInstitutionReq.educationId(), institutionEnt);
        updateActiveStudentOrThrow(addInstitutionReq.studentId(), institutionEnt);

        institutionRepo.save(institutionEnt);

        return institutionTools.mapToDTO(institutionEnt);
    }

    @Override
    @Transactional
    public InstitutionDTO update(
            long id,
            AddInstitutionReq addInstitutionReq
    ) {
        InstitutionEnt institutionEnt = institutionTools.getInstitutionOrThrow(id);

        institutionMapper.updateEntityFromDto(addInstitutionReq, institutionEnt);

        if (!Objects.equals(institutionEnt.getEducation().getId(), addInstitutionReq.educationId())) {
            updateActiveEducationOrThrow(addInstitutionReq.educationId(), institutionEnt);
        }
        if (!Objects.equals(institutionEnt.getStudent().getId(), addInstitutionReq.studentId())) {
            updateActiveStudentOrThrow(addInstitutionReq.studentId(), institutionEnt);
        }

        return institutionTools.mapToDTO(institutionEnt);
    }

    @Override
    @Transactional
    public InstitutionDTO deleteById(long id) {
        InstitutionEnt institutionEnt = institutionTools.getInstitutionOrThrow(id);

        try {
            institutionRepo.delete(institutionEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting institution: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting institution");
        }

        return institutionTools.mapToDTO(institutionEnt);
    }
}
