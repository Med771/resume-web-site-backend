package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.entity.CompanyEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    // ---------------- Entity -> CompanyDTO ----------------
    @Mapping(target = "experiencesId", ignore = true)
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    CompanyDTO toGetRes(CompanyEnt entity);
}