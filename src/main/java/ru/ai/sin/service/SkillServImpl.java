package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;

import ru.ai.sin.entity.SkillEnt;

import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.service.impl.SkillService;
import ru.ai.sin.service.tools.SkillTools;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServImpl implements SkillService {

    private final SkillRepo skillRepo;

    private final SkillMapper skillMapper;

    private final SkillTools skillTools;

    @Override
    public SkillDTO getById(long id) {
        return skillMapper.toDTO(skillTools.getSkillOrThrow(id));
    }

    @Override
    public List<SkillDTO> getAll(
            int pageSkillsNumber,
            int pageSkillsSize
    ) {
        List<SkillEnt> list = skillRepo
                .findAll(PageRequest.of(pageSkillsNumber, pageSkillsSize))
                .stream()
                .toList();

        return list.stream().map(skillMapper::toDTO).toList();
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

        return skillMapper.toDTO(skillEnt);
    }

    @Override
    @Transactional
    public SkillDTO setNameById(
            long id,
            AddSkillReq addSkillReq
    ) {
        SkillEnt skillEnt = skillTools.getSkillOrThrow(id);

        skillEnt.setName(addSkillReq.name());

        return skillMapper.toDTO(skillEnt);
    }

    @Override
    @Transactional
    public SkillDTO deleteById(long id) {
        SkillEnt skillEnt = skillTools.getSkillOrThrow(id);

        try {
            skillRepo.delete(skillEnt);
        }
        catch (DataIntegrityViolationException ex) {
            log.warn("Error while deleting skill: {}", ex.getMessage());

            throw new BadRequestException("Error while deleting skill");
        }

        return skillMapper.toDTO(skillEnt);
    }
}
