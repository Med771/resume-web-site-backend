package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.experience.AddExperienceReq;
import ru.ai.sin.dto.experience.ExperienceRes;
import ru.ai.sin.dto.experience.GetExperienceRes;
import ru.ai.sin.entity.ExperienceEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExperienceMapper {

    // ---------------- AddExperienceReq -> ExperienceEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "student", ignore = true)
    ExperienceEnt toEntity(AddExperienceReq dto);

    // ---------------- ExperienceEnt -> GetExperienceRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "experience.createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "experience.updatedAt")
    GetExperienceRes toGetRes(ExperienceEnt entity);

    // ---------------- ExperienceEnt ->  ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    ExperienceRes toRes(ExperienceEnt entity);

    // ---------------- AddExperienceReq -> ExperienceEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(AddExperienceReq dto, @MappingTarget ExperienceEnt entity);
}
