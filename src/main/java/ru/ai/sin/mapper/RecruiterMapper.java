package ru.ai.sin.mapper;

import org.mapstruct.*;
import ru.ai.sin.dto.recruiter.AddRecruiterReq;
import ru.ai.sin.dto.recruiter.UpdateRecruiterReq;
import ru.ai.sin.dto.recruiter.RecruiterDTO;
import ru.ai.sin.entity.RecruiterEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RecruiterMapper {

    // ---------------- AddRecruiterReq -> RecruiterEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userInformation.passwordHash", ignore = true)
    @Mapping(target = "contactInformation.telegramUserId", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "userInformation.firstName", source = "firstName")
    @Mapping(target = "userInformation.lastName", source = "lastName")
    @Mapping(target = "userInformation.username", source = "username")
    @Mapping(target = "userInformation.email", source = "email")
    @Mapping(target = "contactInformation.phoneNumber", source = "phoneNumber")
    @Mapping(target = "contactInformation.telegramUsername", source = "telegramUsername")
    RecruiterEnt toEntity(AddRecruiterReq dto);

    // ---------------- RecruiterEnt -> RecruiterDTO ----------------
    @Mapping(source = "userInformation.firstName", target = "firstName")
    @Mapping(source = "userInformation.lastName", target = "lastName")
    @Mapping(source = "userInformation.username", target = "username")
    @Mapping(source = "userInformation.email", target = "email")
    @Mapping(source = "contactInformation.phoneNumber", target = "phoneNumber")
    @Mapping(source = "contactInformation.telegramUsername", target = "telegramUsername")
    @Mapping(source = "contactInformation.telegramUserId", target = "telegramUserId")
    RecruiterDTO toDTO(RecruiterEnt entity);

    // ---------------- UpdateRecruiterReq -> RecruiterEnt ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userInformation.passwordHash", ignore = true)
    @Mapping(target = "contactInformation.telegramUserId", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    void updateEntityFromDto(UpdateRecruiterReq dto, @MappingTarget RecruiterEnt entity);

}
