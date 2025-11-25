package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.application.ApplicationDTO;
import ru.ai.sin.entity.ApplicationEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApplicationMapper {
    // ---------------- ApplicationEnt -> ApplicationDTO ----------------
    @Mapping(target = "recruiterId", source = "recruiter.id")
    @Mapping(target = "studentId", source = "student.id")
    ApplicationDTO toDTO(ApplicationEnt entity);
}
