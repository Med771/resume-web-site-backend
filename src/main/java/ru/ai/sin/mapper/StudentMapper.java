package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.student.AddStudentReq;
import ru.ai.sin.dto.student.StudentCardDTO;
import ru.ai.sin.dto.student.StudentDTO;
import ru.ai.sin.entity.StudentEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    // ----------------  -> StudentEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "imagePath", ignore = true)
    @Mapping(target = "speciality",  ignore = true)
    @Mapping(target = "portfolio", ignore = true)
    @Mapping(target = "education", ignore = true)
    @Mapping(target = "companies", ignore = true)
    StudentEnt toEntity(AddStudentReq studentReq);

    // ---------------- StudentEnt -> StudentDTO ----------------
    @Mapping(source = "userInformation.firstName", target = "firstName")
    @Mapping(source = "userInformation.lastName", target = "lastName")
    @Mapping(source = "userInformation.username", target = "username")
    @Mapping(source = "userInformation.email", target = "email")
    @Mapping(source = "contactInformation.phoneNumber", target = "phoneNumber")
    @Mapping(source = "contactInformation.telegramUsername", target = "telegramUsername")
    @Mapping(target = "speciality", ignore = true)
    @Mapping(target = "skills", ignore = true)
    StudentDTO toDTO(StudentEnt student);

    // ---------------- StudentEnt -> StudentCardDTO ----------------
    @Mapping(source = "userInformation.firstName", target = "firstName")
    @Mapping(source = "userInformation.lastName", target = "lastName")
    @Mapping(target = "speciality", ignore = true)
    @Mapping(target = "skills", ignore = true)
    StudentCardDTO toCardDTO(StudentEnt student);
}
