package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.speciality.AddSpecialityReq;
import ru.ai.sin.dto.speciality.SpecialityDTO;
import ru.ai.sin.entity.SpecialityEnt;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecialityMapper {

    // ---------------- AddSpecialityReq -> SpecialityEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    SpecialityEnt toEntity(AddSpecialityReq dto);

    // ---------------- SpecialityEnt -> SpecialityDTO ----------------
    SpecialityDTO toDTO(SpecialityEnt entity, List<SkillDTO> skills);

    // ---------------- AddSpecialityReq -> SpecialityEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    void updateEntityFromDto(AddSpecialityReq dto, @MappingTarget SpecialityEnt entity);
}
