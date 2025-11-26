package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.institution.AddInstitutionReq;
import ru.ai.sin.dto.institution.GetInstitutionRes;
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

    // ---------------- InstitutionEnt -> GetInstitutionRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "institution.createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "institution.updatedAt")
    GetInstitutionRes toGetRes(InstitutionEnt entity);

    // ---------------- InstitutionEnt -> InstitutionRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    InstitutionRes toRes(InstitutionEnt entity);

    // ---------------- AddInstitutionReq -> InstitutionEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDto(AddInstitutionReq dto, @MappingTarget InstitutionEnt entity);
}
