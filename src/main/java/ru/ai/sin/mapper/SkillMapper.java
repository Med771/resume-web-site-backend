package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.skill.AddSkillReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.skill.MergeSkillReq;
import ru.ai.sin.entity.SkillEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {

    // ---------------- AddSkillReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "category", ignore = true)
    SkillEnt toEntity(AddSkillReq dto);

    // ---------------- Entity -> GetSkillRes ----------------
    SkillDTO toGetRes(SkillEnt entity);

    // ---------------- MergeSkillReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "category", ignore = true)
    void updateEntityFromDto(MergeSkillReq dto, @MappingTarget SkillEnt entity);
}