package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.entity.CompanyEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    // ---------------- Entity -> CompanyDTO ----------------
    @Mapping(target = "experiencesId", ignore = true)
    CompanyDTO toDTO(CompanyEnt entity);
}