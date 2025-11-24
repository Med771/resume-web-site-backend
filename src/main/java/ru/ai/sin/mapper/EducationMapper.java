package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.education.AddEducationReq;
import ru.ai.sin.dto.education.GetEducationRes;
import ru.ai.sin.dto.education.MergeEducationReq;
import ru.ai.sin.entity.EducationEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EducationMapper {

    // ---------------- AddEducationReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "education", ignore = true)
    EducationEnt toEntity(AddEducationReq dto);

    // ---------------- Entity -> GetEducationRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetEducationRes toGetRes(EducationEnt entity);

    // ---------------- MergeSkillReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "education", ignore = true)
    void updateEntityFromDto(MergeEducationReq dto, @MappingTarget EducationEnt entity);
}
