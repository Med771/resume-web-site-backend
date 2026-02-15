package ru.ai.sin.logic.user;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import ru.ai.sin.logic.user.dto.UserDTO;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    UserDTO toDTO(UserEnt entity);
}
