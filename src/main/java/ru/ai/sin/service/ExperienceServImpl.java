package ru.ai.sin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.experience.*;
import ru.ai.sin.entity.CompanyEnt;
import ru.ai.sin.entity.ExperienceEnt;
import ru.ai.sin.entity.StudentEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.CompanyMapper;
import ru.ai.sin.mapper.ExperienceMapper;
import ru.ai.sin.repository.CompanyRepo;
import ru.ai.sin.repository.ExperienceRepo;
import ru.ai.sin.repository.StudentRepo;
import ru.ai.sin.service.impl.ExperienceService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExperienceServImpl implements ExperienceService {

    private final ExperienceRepo experienceRepo;
    private final CompanyRepo companyRepo;
    private final StudentRepo studentRepo;

    private final ExperienceMapper experienceMapper;
    private final CompanyMapper companyMapper;

    @Override
    public ExperienceDTO getById(
            long id
    ) {
        ExperienceEnt experienceEnt = experienceRepo.findWithCompanyAndStudentById(id);

        if  (experienceEnt == null) {
            throw new BadRequestException("Failed to find experience by id " + id);
        }

        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }

    @Override
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
                .map(experienceEnt -> {
                    ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

                    return experienceMapper.toDTO(experienceEnt, experienceRes);
                }).toList();
    }

    @Override
    @Transactional
    public ExperienceDTO create(
            AddExperienceReq addExperienceReq
    ) {
        CompanyEnt companyEnt = companyRepo.findByIdAndIsActiveTrue(addExperienceReq.companyId());
        StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(addExperienceReq.studentId());

        if (companyEnt == null) {
            throw new BadRequestException("Failed to find company by id " + addExperienceReq.companyId());
        }
        if (studentEnt == null) {
            throw new BadRequestException("Failed to find student by id " + addExperienceReq.studentId());
        }

        ExperienceEnt experienceEnt = experienceMapper.toEntity(addExperienceReq);

        experienceEnt.setCompany(companyEnt);
        experienceEnt.setStudent(studentEnt);

        experienceEnt = experienceRepo.save(experienceEnt);

        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }

    @Override
    @Transactional
    public ExperienceDTO update(
            long id,
            AddExperienceReq addExperienceReq
    ) {
        ExperienceEnt experienceEnt = experienceRepo.findWithCompanyAndStudentById(id);

        if (experienceEnt == null) {
            throw new BadRequestException("Failed to find experience by id " + id);
        }

        experienceMapper.updateEntityFromDto(addExperienceReq, experienceEnt);

        if (experienceEnt.getCompany().getId() != addExperienceReq.companyId()) {
            CompanyEnt companyEnt = companyRepo.findByIdAndIsActiveTrue(addExperienceReq.companyId());

            if (companyEnt == null) {
                throw new BadRequestException("Failed to find company by id " + addExperienceReq.companyId());
            }

            experienceEnt.setCompany(companyEnt);
        }

        if (experienceEnt.getStudent().getId() != addExperienceReq.studentId()) {
            StudentEnt studentEnt = studentRepo.findByIdAndIsActiveTrue(addExperienceReq.studentId());

            if (studentEnt == null) {
                throw new BadRequestException("Failed to find student by id " + addExperienceReq.studentId());
            }

            experienceEnt.setStudent(studentEnt);
        }

        experienceEnt = experienceRepo.save(experienceEnt);

        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }

    @Override
    @Transactional
    public ExperienceDTO deleteById(
            long id
    ) {
        ExperienceEnt experienceEnt = experienceRepo.findWithCompanyAndStudentById(id);

        if (experienceEnt == null) {
            throw new BadRequestException("Failed to find experience by id " + id);
        }

        experienceRepo.deleteById(id);

        ExperienceRes experienceRes = experienceMapper.toRes(experienceEnt);

        return experienceMapper.toDTO(experienceEnt, experienceRes);
    }
}
