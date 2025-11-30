package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.education.AddEducationReq;
import ru.ai.sin.dto.education.EducationDTO;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.EducationEnt;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EducationMapper {

    // ---------------- AddEducationReq -> EducationEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    @Mapping(target = "institutions", ignore = true)
    EducationEnt toEntity(AddEducationReq dto);

    // ---------------- EducationEnt -> EducationDTO ----------------
    EducationDTO toDTO(EducationEnt entity, List<Long> institutionsIds, List<SkillDTO> skillsIds);

}
