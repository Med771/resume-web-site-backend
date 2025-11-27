package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.institution.AddInstitutionReq;
import ru.ai.sin.dto.institution.InstitutionDTO;
import ru.ai.sin.dto.institution.InstitutionRes;
import ru.ai.sin.entity.InstitutionEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstitutionMapper {

    // ---------------- AddInstitutionReq -> InstitutionEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "student", ignore = true)
    InstitutionEnt toEntity(AddInstitutionReq dto);

    // ---------------- InstitutionEnt -> InstitutionDTO ----------------
    @Mapping(target = "educationId",  ignore = true)
    @Mapping(target = "studentId",  ignore = true)
    InstitutionDTO toGetRes(InstitutionEnt entity);

    // ---------------- InstitutionEnt -> InstitutionRes ----------------
    InstitutionRes toRes(InstitutionEnt entity);
}
