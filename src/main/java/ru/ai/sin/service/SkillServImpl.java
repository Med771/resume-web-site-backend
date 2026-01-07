package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import ru.ai.sin.dto.PageResponse;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.skill.SkillFilterReq;
import ru.ai.sin.dto.skill.UpdateSkillReq;

import ru.ai.sin.entity.SkillEnt;

import ru.ai.sin.exception.models.BadRequestException;

import ru.ai.sin.helper.SecurityHelper;

import ru.ai.sin.mapper.SkillMapper;

import ru.ai.sin.repository.SkillRepo;

import ru.ai.sin.service.impl.SkillService;
import ru.ai.sin.service.tools.SkillTools;


@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServImpl implements SkillService {

    private final SkillRepo skillRepo;

    private final SkillMapper skillMapper;

    private final SkillTools skillTools;

    private final SecurityHelper securityHelper;

    @Override
    public SkillDTO getById(long id) {
        return skillMapper.toDTO(skillTools.getSkillOrThrow(id));
    }

    @Override
    public PageResponse<SkillDTO> getAllByFilter(Pageable pageable, SkillFilterReq skillFilterReq) {
        Page<SkillEnt> page = skillRepo.findAll(
                pageable
        );

        return new PageResponse<>(
                page.getContent().stream().map(skillMapper::toDTO).toList(),
                pageable.getPageNumber(),
                pageable.getPageSize(),
                page.getTotalElements(),
                page.getTotalPages());
    }


    @Override
    public SkillDTO create(AddSkillReq addSkillReq) {
        SkillEnt skillEnt = new SkillEnt(addSkillReq.name());

        try {
            skillEnt = skillRepo.save(skillEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Skill already exists: {}", addSkillReq.name());

            throw new BadRequestException("Skill already exists: " + addSkillReq.name());
        }

        SkillDTO skillDTO = skillMapper.toDTO(skillEnt);

        log.info("User: {}, created a new skill: {}", securityHelper.getCurrentUsername(), skillDTO);

        return skillDTO;
    }



    @Override
    @Transactional
    public SkillDTO updateById(
            long id,
            UpdateSkillReq updateSkillReq
    ) {
        SkillEnt skillEnt = skillTools.getSkillOrThrow(id);

        skillMapper.updateEntityFromDto(updateSkillReq, skillEnt);

        SkillDTO skillDTO = skillMapper.toDTO(skillEnt);

        log.info("User: {}, updated a skill: {} with data: {}", securityHelper.getCurrentUsername(), id, skillDTO);

        return skillDTO;
    }

    @Override
    @Transactional
    public void deleteById(long id) {
        SkillEnt skillEnt = skillTools.getSkillOrThrow(id);

        try {
            skillRepo.delete(skillEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting skill: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting skill");
        }

        log.info("User: {}, deleted a skill: {} with data: {}", securityHelper.getCurrentUsername(), id, skillEnt);
    }
}
