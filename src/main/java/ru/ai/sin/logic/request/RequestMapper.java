package ru.ai.sin.logic.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ru.ai.sin.logic.request.dto.RequestDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RequestMapper {

    // ---------------- RequestEnt -> RequestDTO ----------------
    @Mapping(source = "recruiter.id", target = "recruiterId")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "recruiter.contactInformation.telegramUserId", target = "recruiterTelegramUserId")
    @Mapping(source = "student.contactInformation.telegramUserId", target = "studentTelegramUserId")
    RequestDTO toDTO(RequestEnt entity);
}
