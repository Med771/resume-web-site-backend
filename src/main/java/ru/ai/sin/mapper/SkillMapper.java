package ru.ai.sin.mapper;

import org.mapstruct.*;

import ru.ai.sin.dto.skill.SkillDTO;

import ru.ai.sin.entity.SkillEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {

    // ---------------- SkillEnt -> SkillDTO ----------------
    SkillDTO toDTO(SkillEnt entity);
}