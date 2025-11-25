package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.GetSpecialityRes;
import ru.ai.sin.dto.speciality.MergeSpecialityReq;
import ru.ai.sin.entity.SpecialityEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialityMapper {

    // ---------------- AddSpecialityReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    SpecialityEnt toEntity(AddSpecialityReq dto);

    // ---------------- Entity -> GetSpecialityRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetSpecialityRes toGetRes(SpecialityEnt entity);

    // ---------------- MergeSpecialityReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    void updateEntityFromDto(MergeSpecialityReq dto, @MappingTarget SpecialityEnt entity);
}
