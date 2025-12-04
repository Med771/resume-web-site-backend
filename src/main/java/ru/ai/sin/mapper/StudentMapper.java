package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.dto.student.AddStudentReq;
import ru.ai.sin.dto.student.StudentCardDTO;
import ru.ai.sin.dto.student.StudentDTO;
import ru.ai.sin.entity.StudentEnt;

import java.util.List;

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
    @Mapping(source = "student.userInformation.firstName", target = "firstName")
    @Mapping(source = "student.userInformation.lastName", target = "lastName")
    @Mapping(source = "student.userInformation.username", target = "username")
    @Mapping(source = "student.userInformation.email", target = "email")
    @Mapping(source = "student.contactInformation.phoneNumber", target = "phoneNumber")
    @Mapping(source = "student.contactInformation.telegramUsername", target = "telegramUsername")
    @Mapping(source = "student.speciality.name", target = "speciality")
    StudentDTO toDTO(StudentEnt student, List<SkillDTO> skills);

    // ---------------- StudentEnt -> StudentCardDTO ----------------
    @Mapping(source = "student.userInformation.firstName", target = "firstName")
    @Mapping(source = "student.userInformation.lastName", target = "lastName")
    @Mapping(source = "student.speciality.name", target = "speciality")
    StudentCardDTO toCardDTO(StudentEnt student, List<SkillDTO> skills);
}
