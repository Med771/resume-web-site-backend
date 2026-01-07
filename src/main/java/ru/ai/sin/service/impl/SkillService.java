package ru.ai.sin.service.impl;

import org.springframework.data.domain.Pageable;

import ru.ai.sin.dto.PageResponse;

import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.skill.SkillFilterReq;
import ru.ai.sin.dto.skill.UpdateSkillReq;


public interface SkillService {

    // ---------- GET METHODS ----------
    SkillDTO getById(long id);

    // ---------- POST METHODS ----------
    PageResponse<SkillDTO> getAllByFilter(
            Pageable pageable,
            SkillFilterReq skillFilterReq);

    SkillDTO create(AddSkillReq addSkillReq);

    SkillDTO updateById(
            long id,
            UpdateSkillReq updateSkillReq);

    // ---------- DELETE METHODS ----------
    void deleteById(long id);
}
