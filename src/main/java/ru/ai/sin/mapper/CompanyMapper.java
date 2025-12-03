package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.company.CompanyDTO;
import ru.ai.sin.entity.CompanyEnt;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CompanyMapper {

    // ---------------- CompanyEnt -> CompanyDTO ----------------
    CompanyDTO toDTO(CompanyEnt entity, List<Long> experiencesIds);
}