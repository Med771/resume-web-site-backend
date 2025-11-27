package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.education.AddEducationReq;
import ru.ai.sin.dto.education.EducationDTO;
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

    // ---------------- Entity -> EducationDTO ----------------
    @Mapping(target = "studentIds",  ignore = true)
    @Mapping(target = "skillsIds", ignore = true)
    EducationDTO toDTO(EducationEnt entity);

}
