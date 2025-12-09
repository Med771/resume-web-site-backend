package ru.ai.sin.service.impl;

import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;

import java.util.List;

public interface SkillService {

    // ---------- GET METHODS ----------
    SkillDTO getById(
            long id);

    List<SkillDTO> getAll(
            int pageSkillsNumber, int pageSkillsSize);

    // ---------- POST METHODS ----------
    SkillDTO create(
            AddSkillReq addSkillReq);

    SkillDTO setNameById(
            long id,
            AddSkillReq addSkillReq);

    // ---------- DELETE METHODS ----------
    SkillDTO deleteById(
            long id);
}
