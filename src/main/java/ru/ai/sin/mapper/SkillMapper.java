package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.GetSkillRes;
import ru.ai.sin.dto.skill.MergeSkillReq;
import ru.ai.sin.entity.SkillEnt;

@Mapper(componentModel = "spring")
public interface SkillMapper {

    // ---------------- AddSkillReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    SkillEnt toEntity(AddSkillReq dto);

    // ---------------- Entity -> GetSkillRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetSkillRes toGetRes(SkillEnt entity);

    // ---------------- MergeSkillReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateEntityFromDto(MergeSkillReq dto, @MappingTarget SkillEnt entity);
}