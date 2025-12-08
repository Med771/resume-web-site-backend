package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.institution.*;
import ru.ai.sin.entity.EducationEnt;
import ru.ai.sin.entity.InstitutionEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.EducationMapper;
import ru.ai.sin.mapper.InstitutionMapper;
import ru.ai.sin.repository.EducationRepo;
import ru.ai.sin.repository.InstitutionRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.InstitutionService;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionServImpl implements InstitutionService {

    private final InstitutionRepo institutionRepo;
    private final EducationRepo educationRepo;
    private final StudentRepo studentRepo;

    private final InstitutionMapper institutionMapper;
    private final EducationMapper educationMapper;

    private InstitutionEnt getActiveInstitutionOrThrow(long id) {
        InstitutionEnt institutionEnt = institutionRepo.findWithEducationAndStudentById(id);

        if (institutionEnt == null) {
            throw new NotFoundException("Failed to find institution with id: " + id);
        }

        return institutionEnt;
    }

    private InstitutionDTO mapToDTO(InstitutionEnt institutionEnt) {
        InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

        return institutionMapper.toDTO(institutionEnt, institutionRes);
    }

    private void updateActiveEducationOrThrow(long educationId, InstitutionEnt institutionEnt) {
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(educationId);

        if (educationEnt == null) {
            throw new NotFoundException("Failed to find education with id: " + educationId);
        }

        institutionEnt.setEducation(educationEnt);
    }

    private void updateActiveStudentOrThrow(UUID studentId, InstitutionEnt institutionEnt) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(studentId);

        if (studentEnt == null) {
            throw new NotFoundException("Failed to find student with id: " + studentId);
        }

        institutionEnt.setStudent(studentEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public InstitutionDTO getById(
            long id
    ) {
        InstitutionEnt institutionEnt = getActiveInstitutionOrThrow(id);

        return mapToDTO(institutionEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAboutEducationRes getByEducationId(
            long id,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        Pageable pageable = PageRequest.of(pageInstitutionNumber, pageInstitutionSize);

        List<InstitutionEnt> institutionEntList = institutionRepo.findAllByEducationId(id, pageable).getContent();

        List<GetEducationInstitutionRes> getEducationInstitutionResList = institutionEntList.stream()
                .map(institutionEnt -> new GetEducationInstitutionRes(
                        institutionEnt.getStudent().getId(),
                        institutionMapper.toRes(institutionEnt)
                )).toList();

        return new GetAboutEducationRes(id, getEducationInstitutionResList);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAboutStudentRes getByStudentId(
            UUID id,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        Pageable pageable = PageRequest.of(pageInstitutionNumber, pageInstitutionSize);

        List<InstitutionEnt> institutionEntList = institutionRepo.findAllByStudentId(id, pageable).getContent();

        List<GetStudentInstitutionRes> getStudentInstitutionResList = institutionEntList.stream()
                .map(institutionEnt -> new GetStudentInstitutionRes(
                        educationMapper.toRes(institutionEnt.getEducation()),
                        institutionMapper.toRes(institutionEnt)
                )).toList();

        return new GetAboutStudentRes(id, getStudentInstitutionResList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InstitutionDTO> getAll(
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        Pageable pageable = PageRequest.of(pageInstitutionNumber, pageInstitutionSize);

        List<InstitutionEnt> institutionEntList = institutionRepo.findAll(pageable).getContent();

        return institutionEntList.stream()
                .map(institutionEnt -> {
                    InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

                    return institutionMapper.toDTO(institutionEnt, institutionRes);
                }).toList();
    }

    @Override
    @Transactional
    public InstitutionDTO create(
            AddInstitutionReq addInstitutionReq
    ) {
        InstitutionEnt institutionEnt = institutionMapper.toEntity(addInstitutionReq);

        updateActiveEducationOrThrow(addInstitutionReq.educationId(), institutionEnt);
        updateActiveStudentOrThrow(addInstitutionReq.studentId(), institutionEnt);

        institutionRepo.save(institutionEnt);

        return mapToDTO(institutionEnt);
    }

    @Override
    @Transactional
    public InstitutionDTO update(
            long id,
            AddInstitutionReq addInstitutionReq
    ) {
        InstitutionEnt institutionEnt = institutionRepo.findWithEducationAndStudentById(id);

        if (institutionEnt == null) {
            throw new BadRequestException("Failed to find education by id: " + id);
        }

        institutionMapper.updateEntityFromDto(addInstitutionReq, institutionEnt);

        if (!Objects.equals(institutionEnt.getEducation().getId(), addInstitutionReq.educationId())) {
            updateActiveEducationOrThrow(addInstitutionReq.educationId(), institutionEnt);
        }
        if (!Objects.equals(institutionEnt.getStudent().getId(), addInstitutionReq.studentId())) {
            updateActiveStudentOrThrow(addInstitutionReq.studentId(), institutionEnt);
        }

        return mapToDTO(institutionEnt);
    }

    @Override
    @Transactional
    public InstitutionDTO deleteById(
            long id
    ) {
        InstitutionEnt institutionEnt = getActiveInstitutionOrThrow(id);

        institutionRepo.delete(institutionEnt);

        return mapToDTO(institutionEnt);
    }
}
