package ru.ai.sin.logic.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import ru.ai.sin.logic.request.dto.RequestDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface RequestMapper {

    // ---------------- RequestEnt -> RequestDTO ----------------
    @Mapping(source = "id", target = "id")
    @Mapping(source = "chatId", target = "chatId")
    @Mapping(source = "chatTitle", target = "chatTitle")
    @Mapping(source = "result", target = "result")
    @Mapping(source = "chatUrl", target = "chatUrl")
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    @Mapping(source = "studentResponseText", target = "studentResponseText")
    @Mapping(source = "hasRecruiterMessage", target = "hasRecruiterMessage")
    @Mapping(source = "hasStudentMessage", target = "hasStudentMessage")
    @Mapping(source = "recruiter.id", target = "recruiterId")
    @Mapping(source = "recruiter.contactInformation.telegramUserId", target = "recruiterTelegramUserId")
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.contactInformation.telegramUserId", target = "studentTelegramUserId")
    RequestDTO toDTO(RequestEnt entity);
}
