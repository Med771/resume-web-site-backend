package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.experience.*;
import ru.ai.sin.entity.ExperienceEnt;
import ru.ai.sin.entity.StudentEnt;

import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.CompanyMapper;
import ru.ai.sin.mapper.ExperienceMapper;
import ru.ai.sin.repository.ExperienceRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.ExperienceService;
import ru.ai.sin.service.tools.CompanyTools;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceServImpl implements ExperienceService {

    private final ExperienceRepo experienceRepo;
    private final StudentRepo studentRepo;

    private final ExperienceMapper experienceMapper;
    private final CompanyMapper companyMapper;

    private final CompanyTools companyTools;

    private ExperienceEnt getActiveExperienceOrThrow(long id) {
        ExperienceEnt experienceEnt = experienceRepo.findWithCompanyAndStudentById(id);

        if  (experienceEnt == null) {
            throw new NotFoundException("Failed to find experience by id " + id);
        }

        return experienceEnt;
    }

    private ExperienceDTO mapToDTO(ExperienceEnt experienceEnt) {
        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }

    private void updateActiveCompanyOrThrow(long companyId, ExperienceEnt experienceEnt) {
        experienceEnt.setCompany(companyTools.getCompanyOrThrow(companyId));
    }

    private void updateActiveStudentOrThrow(UUID studentId, ExperienceEnt experienceEnt) {
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(studentId);

        if (studentEnt == null) {
            throw new NotFoundException("Failed to find student by id " + studentId);
        }

        experienceEnt.setStudent(studentEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public ExperienceDTO getById(
            long id
    ) {
        ExperienceEnt experienceEnt = getActiveExperienceOrThrow(id);

        return mapToDTO(experienceEnt);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAboutCompanyRes getAboutCompanyById(
            long id,
            int pageExperienceNumber, int pageExperienceSize
    ) {
        Pageable pageable = PageRequest.of(pageExperienceNumber, pageExperienceSize);

        List<ExperienceEnt> experienceEntList = experienceRepo.findAllByCompanyId(id, pageable).getContent();

        List<GetCompanyExperienceRes> getCompanyExperienceRes = experienceEntList.stream()
                .map(experienceEnt -> new GetCompanyExperienceRes(
                        experienceEnt.getStudent().getId(),
                        experienceMapper.toRes(experienceEnt)
                )).toList();

        return new GetAboutCompanyRes(id, getCompanyExperienceRes);
    }

    @Override
    @Transactional(readOnly = true)
    public GetAboutStudentRes getAboutStudentById(
            UUID id,
            int pageExperienceNumber, int pageExperienceSize
    ) {
        Pageable pageable = PageRequest.of(pageExperienceNumber, pageExperienceSize);

        List<ExperienceEnt> experienceEntList = experienceRepo.findAllByStudentId(id, pageable).getContent();

        List<GetStudentExperienceRes> getStudentExperienceRes = experienceEntList.stream()
                .map(experienceEnt -> new GetStudentExperienceRes(
                        companyMapper.toRes(experienceEnt.getCompany()),
                        experienceMapper.toRes(experienceEnt)
                )).toList();

        return new GetAboutStudentRes(id, getStudentExperienceRes);
    }

    @Override
    public List<ExperienceDTO> getAll(
            int pageExperienceNumber, int pageExperienceSize
    ) {
        Pageable pageable = PageRequest.of(pageExperienceNumber, pageExperienceSize);

        List<ExperienceEnt> experienceEntList = experienceRepo.findAll(pageable).getContent();

        return experienceEntList.stream()
                .map(this::mapToDTO).toList();
    }

    @Override
    @Transactional
    public ExperienceDTO create(
            AddExperienceReq addExperienceReq
    ) {
        ExperienceEnt experienceEnt = experienceMapper.toEntity(addExperienceReq);

        updateActiveCompanyOrThrow(addExperienceReq.companyId(), experienceEnt);
        updateActiveStudentOrThrow(addExperienceReq.studentId(), experienceEnt);

        experienceEnt = experienceRepo.save(experienceEnt);

        return mapToDTO(experienceEnt);
    }

    @Override
    @Transactional
    public ExperienceDTO update(
            long id,
            AddExperienceReq addExperienceReq
    ) {
        ExperienceEnt experienceEnt = getActiveExperienceOrThrow(id);

        experienceMapper.updateEntityFromDto(addExperienceReq, experienceEnt);

        if (!Objects.equals(experienceEnt.getCompany().getId(), addExperienceReq.companyId())) {
            updateActiveCompanyOrThrow(addExperienceReq.companyId(), experienceEnt);
        }

        if (!Objects.equals(experienceEnt.getStudent().getId(), addExperienceReq.studentId())) {
            updateActiveStudentOrThrow(addExperienceReq.studentId(), experienceEnt);
        }

        return mapToDTO(experienceEnt);
    }

    @Override
    @Transactional
    public ExperienceDTO deleteById(
            long id
    ) {
        ExperienceEnt experienceEnt = getActiveExperienceOrThrow(id);

        experienceRepo.deleteById(id);

        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }
}
