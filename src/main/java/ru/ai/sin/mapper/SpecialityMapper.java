package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;
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
    @Mapping(target = "skills", ignore = true)
    SpecialityDTO toDTO(SpecialityEnt entity);
}
