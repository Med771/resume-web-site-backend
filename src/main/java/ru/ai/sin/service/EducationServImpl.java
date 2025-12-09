package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.education.*;

import ru.ai.sin.entity.EducationEnt;

import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.EducationMapper;

import ru.ai.sin.repository.EducationRepo;

import ru.ai.sin.service.impl.EducationService;
import ru.ai.sin.tools.EducationTools;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EducationServImpl implements EducationService {

    private final EducationRepo educationRepo;

    private final EducationMapper educationMapper;

    private final EducationTools educationTools;

    @Override
    @Transactional(readOnly = true)
    public EducationDTO getById(long id) {
        return educationMapper.toDTO(educationTools.getEducationOrThrow(id));
    }

    @Override
    public List<EducationDTO> getAll(
            int pageEducationNumber,
            int pageEducationSize
    ) {
        Page<EducationEnt> educationPage = educationRepo.findAll(
                PageRequest.of(pageEducationNumber, pageEducationSize));

        return educationPage.getContent().stream()
                .map(educationMapper::toDTO)
                .toList();
    }

    @Override
    public EducationDTO create(AddEducationReq addEducationReq) {
        EducationEnt educationEnt = educationMapper.toEntity(addEducationReq);

        educationEnt = educationRepo.save(educationEnt);

        return educationMapper.toDTO(educationEnt);
    }

    @Override
    @Transactional
    public EducationDTO update(
            long id,
            AddEducationReq addEducationReq
    ) {
        EducationEnt educationEnt = educationTools.getEducationOrThrow(id);

        educationMapper.updateEntityFromDto(addEducationReq, educationEnt);

        return educationMapper.toDTO(educationEnt);
    }

    @Override
    @Transactional
    public EducationDTO deleteById(long id) {
        EducationEnt educationEnt = educationTools.getEducationOrThrow(id);

        try {
            educationRepo.deleteById(id);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting education: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting education");
        }


        return educationMapper.toDTO(educationEnt);
    }
}
