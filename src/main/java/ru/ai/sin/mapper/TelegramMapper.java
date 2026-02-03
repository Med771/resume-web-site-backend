package ru.ai.sin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import ru.ai.sin.dto.telegram.OffersDTO;
import ru.ai.sin.dto.telegram.RecruiterTelegramDTO;
import ru.ai.sin.dto.telegram.StudentTelegramDTO;
import ru.ai.sin.entity.RecruiterEnt;
import ru.ai.sin.entity.RequestEnt;
import ru.ai.sin.entity.StudentEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TelegramMapper {

    // ---------------- StudentEnt -> StudentTelegramDTO ----------------
    @Mapping(source = "contactInformation.telegramUsername", target = "telegramUsername")
    @Mapping(source = "contactInformation.telegramUserId", target = "telegramUserId")
    StudentTelegramDTO studentToDTO(StudentEnt entity);

    // ---------------- RecruiterEnt -> RecruiterTelegramDTO ----------------
    @Mapping(source = "contactInformation.telegramUsername", target = "telegramUsername")
    @Mapping(source = "contactInformation.telegramUserId", target = "telegramUserId")
    RecruiterTelegramDTO recruiterToDTO(RecruiterEnt entity);

    // ---------------- RequestEnt -> OffersDTO.Offer ----------------
    @Mapping(target = "studentRes", source = "student")
    @Mapping(target = "recruiterRes", source = "recruiter")
    OffersDTO.Offer toOffer(RequestEnt entity);
}

