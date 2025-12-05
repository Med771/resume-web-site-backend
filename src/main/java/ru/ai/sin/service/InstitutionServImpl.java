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
import ru.ai.sin.mapper.InstitutionMapper;
import ru.ai.sin.repository.EducationRepo;
import ru.ai.sin.repository.InstitutionRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.InstitutionService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class InstitutionServImpl implements InstitutionService {

    private final InstitutionRepo institutionRepo;
    private final EducationRepo educationRepo;
    private final StudentRepo studentRepo;

    private final InstitutionMapper institutionMapper;

    @Override
    public InstitutionDTO getById(
            long id) {
        InstitutionEnt institutionEnt = institutionRepo.findWithEducationAndStudentById(id);

        if (institutionEnt == null) {
            throw new BadRequestException("Failed to find institution with id: " + id);
        }

        InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

        return institutionMapper.toDTO(institutionEnt, institutionRes);
    }

    @Override
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
    public GetAboutStudentRes getByStudentId(
            UUID id,
            int pageInstitutionNumber, int pageInstitutionSize
    ) {
        Pageable pageable = PageRequest.of(pageInstitutionNumber, pageInstitutionSize);

        List<InstitutionEnt> institutionEntList = institutionRepo.findAllByStudentId(id, pageable).getContent();

        List<GetStudentInstitutionRes> getStudentInstitutionResList = institutionEntList.stream()
                .map(institutionEnt -> new GetStudentInstitutionRes(
                        institutionEnt.getEducation().getId(),
                        institutionMapper.toRes(institutionEnt)
                )).toList();

        return new GetAboutStudentRes(id, getStudentInstitutionResList);
    }

    @Override
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
        EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(addInstitutionReq.educationId());
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(addInstitutionReq.studentId());

        if (educationEnt == null) {
            throw new BadRequestException("Failed to find education by id: " + addInstitutionReq.educationId());
        }
        if (studentEnt == null) {
            throw new BadRequestException("Failed to find student by id: " + addInstitutionReq.studentId());
        }

        InstitutionEnt institutionEnt = institutionMapper.toEntity(addInstitutionReq);

        institutionEnt.setEducation(educationEnt);
        institutionEnt.setStudent(studentEnt);

        institutionRepo.save(institutionEnt);

        InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

        return institutionMapper.toDTO(institutionEnt, institutionRes);
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

        if (institutionEnt.getEducation().getId() != addInstitutionReq.educationId()) {
            EducationEnt educationEnt = educationRepo.findByIdAndIsActiveTrue(addInstitutionReq.educationId());

            if (educationEnt == null) {
                throw new BadRequestException("Failed to find education by id: " + id);
            }

            institutionEnt.setEducation(educationEnt);
        }
        if (institutionEnt.getStudent().getId() != addInstitutionReq.studentId()) {
            StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(addInstitutionReq.studentId());

            if (studentEnt == null) {
                throw new BadRequestException("Failed to find student by id " + addInstitutionReq.studentId());
            }

            institutionEnt.setStudent(studentEnt);
        }

        institutionRepo.save(institutionEnt);

        InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

        return institutionMapper.toDTO(institutionEnt, institutionRes);
    }

    @Override
    public InstitutionDTO deleteById(
            long id
    ) {
        InstitutionEnt institutionEnt = institutionRepo.findWithEducationAndStudentById(id);

        if (institutionEnt == null) {
            throw new BadRequestException("Failed to find institution with id: " + id);
        }

        institutionRepo.delete(institutionEnt);

        InstitutionRes institutionRes = institutionMapper.toRes(institutionEnt);

        return institutionMapper.toDTO(institutionEnt, institutionRes);
    }
}
