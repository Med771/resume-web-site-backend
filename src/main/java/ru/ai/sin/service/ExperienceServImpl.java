package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.experience.*;
import ru.ai.sin.entity.ExperienceEnt;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.mapper.CompanyMapper;
import ru.ai.sin.mapper.ExperienceMapper;

import ru.ai.sin.repository.ExperienceRepo;

import ru.ai.sin.service.impl.ExperienceService;

import ru.ai.sin.service.tools.CompanyTools;
import ru.ai.sin.service.tools.ExperienceTools;
import ru.ai.sin.service.tools.StudentTools;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceServImpl implements ExperienceService {

    private final ExperienceRepo experienceRepo;

    private final ExperienceMapper experienceMapper;
    private final CompanyMapper companyMapper;

    private final ExperienceTools experienceTools;
    private final CompanyTools companyTools;
    private final StudentTools studentTools;

    private void updateActiveCompanyOrThrow(long companyId, ExperienceEnt experienceEnt) {
        experienceEnt.setCompany(companyTools.getCompanyOrThrow(companyId));
    }

    private void updateActiveStudentOrThrow(UUID studentId, ExperienceEnt experienceEnt) {
        experienceEnt.setStudent(studentTools.getStudentOrThrow(studentId));
    }

    @Override
    public ExperienceDTO getById(long id) {
        return experienceTools.mapToDTO(experienceTools.getExperienceOrThrow(id));
    }

    @Override
    public GetAboutCompanyRes getAboutCompanyById(
            long id,
            int pageExperienceNumber,
            int pageExperienceSize
    ) {

        List<ExperienceEnt> experienceEntList = experienceRepo
                .findAllByCompanyId(
                        id,
                        PageRequest.of(pageExperienceNumber, pageExperienceSize))
                .getContent();

        List<GetAboutCompanyRes.GetCompanyExperienceRes> getCompanyExperienceRes = experienceEntList.stream()
                .map(experienceEnt -> new GetAboutCompanyRes.GetCompanyExperienceRes(
                        experienceEnt.getStudent().getId(),
                        experienceMapper.toRes(experienceEnt)
                )).toList();

        return new GetAboutCompanyRes(id, getCompanyExperienceRes);
    }

    @Override
    public GetAboutStudentRes getAboutStudentById(
            UUID id,
            int pageExperienceNumber,
            int pageExperienceSize
    ) {
        List<ExperienceEnt> experienceEntList = experienceRepo
                .findAllByStudentId(
                        id,
                        PageRequest.of(pageExperienceNumber, pageExperienceSize))
                .getContent();

        List<GetAboutStudentRes.GetStudentExperienceRes> getStudentExperienceRes = experienceEntList.stream()
                .map(experienceEnt -> new GetAboutStudentRes.GetStudentExperienceRes(
                        companyMapper.toRes(experienceEnt.getCompany()),
                        experienceMapper.toRes(experienceEnt)
                )).toList();

        return new GetAboutStudentRes(id, getStudentExperienceRes);
    }

    @Override
    public List<ExperienceDTO> getAll(
            int pageExperienceNumber,
            int pageExperienceSize
    ) {
        List<ExperienceEnt> experienceEntList = experienceRepo
                .findAll(
                        PageRequest.of(pageExperienceNumber, pageExperienceSize))
                .getContent();

        return experienceEntList.stream()
                .map(experienceTools::mapToDTO).toList();
    }

    @Override
    @Transactional
    public ExperienceDTO create(AddExperienceReq addExperienceReq) {
        ExperienceEnt experienceEnt = experienceMapper.toEntity(addExperienceReq);

        updateActiveCompanyOrThrow(addExperienceReq.companyId(), experienceEnt);
        updateActiveStudentOrThrow(addExperienceReq.studentId(), experienceEnt);

        experienceEnt = experienceRepo.save(experienceEnt);

        return experienceTools.mapToDTO(experienceEnt);
    }

    @Override
    @Transactional
    public ExperienceDTO update(
            long id,
            AddExperienceReq addExperienceReq
    ) {
        ExperienceEnt experienceEnt = experienceTools.getExperienceOrThrow(id);

        experienceMapper.updateEntityFromDto(addExperienceReq, experienceEnt);

        if (!Objects.equals(experienceEnt.getCompany().getId(), addExperienceReq.companyId())) {
            updateActiveCompanyOrThrow(addExperienceReq.companyId(), experienceEnt);
        }

        if (!Objects.equals(experienceEnt.getStudent().getId(), addExperienceReq.studentId())) {
            updateActiveStudentOrThrow(addExperienceReq.studentId(), experienceEnt);
        }

        return experienceTools.mapToDTO(experienceEnt);
    }

    @Override
    @Transactional
    public ExperienceDTO deleteById(long id) {
        ExperienceEnt experienceEnt = experienceTools.getExperienceOrThrow(id);

        try {
            experienceRepo.delete(experienceEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting experience: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting experience");
        }

        return experienceTools.mapToDTO(experienceEnt);
    }
}
