package ru.ai.sin.mapper;

import org.mapstruct.*;

import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;

import ru.ai.sin.entity.SpecialityEnt;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialityMapper {

    // ---------------- AddSpecialityReq -> SpecialityEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    SpecialityEnt toEntity(AddSpecialityReq dto);

    // ---------------- SpecialityEnt -> SpecialityDTO ----------------
    SpecialityDTO toDTO(SpecialityEnt entity);

    // ---------------- AddSpecialityReq -> SpecialityEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    void updateEntityFromDto(AddSpecialityReq dto, @MappingTarget SpecialityEnt entity);
}
