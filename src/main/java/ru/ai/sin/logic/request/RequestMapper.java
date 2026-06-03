package ru.ai.sin.logic.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ru.ai.sin.logic.request.dto.RequestDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RequestMapper {

    @Mapping(source = "appChat.id", target = "appChatId")
    @Mapping(source = "result", target = "result")
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    @Mapping(source = "studentResponseText", target = "studentResponseText")
    @Mapping(source = "recruiter.id", target = "recruiterId")
    @Mapping(source = "student.id", target = "studentId")
    RequestDTO toDTO(RequestEnt entity);
}
