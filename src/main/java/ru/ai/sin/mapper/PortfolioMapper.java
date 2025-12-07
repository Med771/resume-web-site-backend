package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.portfolio.AddPortfolioReq;
import ru.ai.sin.dto.portfolio.PortfolioDTO;
import ru.ai.sin.entity.PortfolioEnt;
import ru.ai.sin.entity.StudentEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PortfolioMapper {

    // ---------------- AddSpecialityReq -> PortfolioEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    PortfolioEnt toEntity(AddPortfolioReq dto, StudentEnt student);

    // ---------------- PortfolioEnt -> PortfolioDTO ----------------
    @Mapping(target = "studentId", source = "entity.student.id")
    PortfolioDTO toDTO(PortfolioEnt entity);
}
