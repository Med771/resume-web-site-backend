package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.company.AddCompanyReq;
import ru.ai.sin.dto.company.GetCompanyRes;
import ru.ai.sin.dto.company.MergeCompanyReq;
import ru.ai.sin.entity.CompanyEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    // ---------------- AddCompanyReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    CompanyEnt toEntity(AddCompanyReq dto);

    // ---------------- Entity -> GetCompanyRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetCompanyRes toGetRes(CompanyEnt entity);

    // ---------------- MergeCompanyReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    void updateEntityFromDto(MergeCompanyReq dto, @MappingTarget CompanyEnt entity);
}