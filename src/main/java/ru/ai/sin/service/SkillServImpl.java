package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SetSkillNameReq;
import ru.ai.sin.dto.skill.SkillDTO;

import ru.ai.sin.entity.SkillEnt;

import ru.ai.sin.exception.models.NotFoundException;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.service.impl.SkillService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServImpl implements SkillService {

    private final SkillRepo skillRepo;

    private final SkillMapper skillMapper;

    private SkillEnt getActiveSkillOrThrow(long id) {
        return skillRepo.findByIdAndIsActiveTrue(id).orElseThrow(
                () -> new NotFoundException("Failed find by id")
        );
    }

    @Override
    public SkillDTO getById(long id) {
        return skillMapper.toDTO(getActiveSkillOrThrow(id));
    }

    @Override
    public List<SkillDTO> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<SkillEnt> list = skillRepo.findAllByIsActiveTrue(pageRequest).stream().toList();

        return list.stream().map(skillMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public SkillDTO create(AddSkillReq addSkillReq) {
        Optional<SkillEnt> optSkillEnt = skillRepo.findByNameIgnoreCase(addSkillReq.name());

        SkillEnt skillEnt;

        if (optSkillEnt.isPresent()) {
            skillEnt = optSkillEnt.get();

            skillEnt.setIsActive(true);
        }
        else {
            skillEnt = skillMapper.toEntity(addSkillReq);
        }

        skillEnt = skillRepo.save(skillEnt);

        return skillMapper.toDTO(skillEnt);
    }

    @Override
    @Transactional
    public SkillDTO setNameById(long id, SetSkillNameReq setSkillNameReq) {
        SkillEnt skillEnt = getActiveSkillOrThrow(id);

        skillEnt.setName(setSkillNameReq.name());

        skillEnt = skillRepo.save(skillEnt);

        return skillMapper.toDTO(skillEnt);
    }

    @Override
    @Transactional
    public SkillDTO deleteById(long id) {
        SkillEnt skillEnt = getActiveSkillOrThrow(id);

        skillEnt.setIsActive(false);

        return skillMapper.toDTO(skillEnt);
    }
}
