package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.entity.PortfolioEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PortfolioMapper {

    // ---------------- AddSpecialityReq -> PortfolioEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "student", ignore = true)
    PortfolioEnt toEntity(AddPortfolioReq dto);

    // ---------------- PortfolioEnt -> PortfolioDTO ----------------
    @Mapping(target = "studentId", ignore = true)
    PortfolioDTO toDTO(PortfolioEnt entity);
}
