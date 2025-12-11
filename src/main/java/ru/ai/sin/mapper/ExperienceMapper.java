package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import ru.ai.sin.dto.experience.AddExperienceReq;
import ru.ai.sin.dto.experience.ExperienceRes;
import ru.ai.sin.dto.experience.ExperienceDTO;

import ru.ai.sin.entity.ExperienceEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExperienceMapper {

    // ---------------- AddExperienceReq -> ExperienceEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "student", ignore = true)
    ExperienceEnt toEntity(AddExperienceReq dto);

    // ---------------- ExperienceEnt -> ExperienceDTO ----------------
    @Mapping(target = "companyId", source = "entity.company.id")
    @Mapping(target = "studentId", source = "entity.student.id")
    ExperienceDTO toDTO(ExperienceEnt entity, ExperienceRes experience);

    // ---------------- ExperienceEnt -> ExperienceRes ----------------
    ExperienceRes toRes(ExperienceEnt entity);

    // ---------------- AddExperienceReq -> ExperienceEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "company", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(AddExperienceReq dto, @MappingTarget ExperienceEnt entity);
}
