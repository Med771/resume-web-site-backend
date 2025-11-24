package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.GetPortfolioRes;
import ru.ai.sin.dto.portfolio.MergePortfolioReq;
import ru.ai.sin.entity.PortfolioEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PortfolioMapper {

    // ---------------- AddSpecialityReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    PortfolioEnt toEntity(AddPortfolioReq dto);

    // ---------------- Entity -> GetPortfolioRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetPortfolioRes toGetRes(PortfolioEnt entity);

    // ----------------  -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    void updateEntityFromDto(MergePortfolioReq dto, @MappingTarget PortfolioEnt entity);
}
