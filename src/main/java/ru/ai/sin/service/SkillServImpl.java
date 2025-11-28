package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SetSkillNameReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.service.impl.SkillService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillServImpl implements SkillService {

    private final SkillRepo skillRepo;


    @Override
    public SkillDTO getById(long id) {
        return null;
    }

    @Override
    public List<SkillDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public SkillDTO create(AddSkillReq addSkillReq) {
        return null;
    }

    @Override
    public SkillDTO setNameById(long id, SetSkillNameReq setSkillNameReq) {
        return null;
    }

    @Override
    public SkillDTO deleteById(long id) {
        return null;
    }
}
