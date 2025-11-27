package ru.ai.sin.mapper;

import org.mapstruct.*;

import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;

import ru.ai.sin.entity.SkillEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {

    // ---------------- AddSkillReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    SkillEnt toEntity(AddSkillReq dto);

    // ---------------- Entity -> GetSkillRes ----------------
    SkillDTO toGetRes(SkillEnt entity);
}