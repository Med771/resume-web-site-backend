package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.GetRecruiterRes;
import ru.ai.sin.dto.recruiter.MergeRecruiterReq;
import ru.ai.sin.entity.RecruiterEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecruiterMapper {

    // ---------------- AddRecruiterReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "userInformation.passwordHash", ignore = true)
    @Mapping(target = "contactInformation.telegramUserId", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    RecruiterEnt toEntity(AddRecruiterReq dto);

    // ---------------- Entity -> GetEducationRes ----------------
    @Mapping(source = "userInformation.firstName", target = "firstName")
    @Mapping(source = "userInformation.lastName", target = "lastName")
    @Mapping(source = "userInformation.username", target = "username")
    @Mapping(source = "userInformation.email", target = "email")
    @Mapping(source = "contactInformation.phoneNumber", target = "phoneNumber")
    @Mapping(source = "contactInformation.telegramUsername", target = "telegramUsername")
    @Mapping(source = "contactInformation.telegramUserId", target = "telegramUserId")
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetRecruiterRes toGetRes(RecruiterEnt entity);

    // ---------------- MergeRecruiterReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "userInformation.passwordHash", ignore = true)
    @Mapping(target = "contactInformation.telegramUserId", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    void updateEntityFromDto(MergeRecruiterReq dto, @MappingTarget RecruiterEnt entity);
}
