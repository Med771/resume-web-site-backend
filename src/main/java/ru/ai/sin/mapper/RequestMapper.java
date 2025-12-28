package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.request.RequestDTO;
import ru.ai.sin.entity.RequestEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {

    // ---------------- RequestEnt -> RequestDTO ----------------
    @Mapping(source = "recruiter.id", target = "recruiterId")
    @Mapping(source = "recruiter.contactInformation.telegramUserId", target = "recruiterTelegramUserId")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.contactInformation.telegramUserId", target = "studentTelegramUserId")
    RequestDTO toDTO(RequestEnt entity);
}
