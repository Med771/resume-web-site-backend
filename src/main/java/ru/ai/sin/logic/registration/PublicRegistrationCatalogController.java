package ru.ai.sin.logic.registration;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.ai.sin.config.property.RegistrationProperties;
import ru.ai.sin.models.PageResponse;
import ru.ai.sin.logic.company.CompanyService;
import ru.ai.sin.logic.company.dto.CompanyDTO;
import ru.ai.sin.logic.company.dto.FilterCompanyReq;
import ru.ai.sin.logic.education.EducationService;
import ru.ai.sin.logic.education.dto.EducationDTO;
import ru.ai.sin.logic.education.dto.FilterEducationReq;
import ru.ai.sin.logic.skill.SkillService;
import ru.ai.sin.logic.skill.dto.FilterSkillReq;
import ru.ai.sin.logic.skill.dto.SkillDTO;
import ru.ai.sin.logic.speciality.SpecialityService;
import ru.ai.sin.logic.speciality.dto.FilterSpecialityReq;
import ru.ai.sin.logic.speciality.dto.SpecialityDTO;

@RestController
@RequestMapping("/public/registration")
@RequiredArgsConstructor
@Tag(name = "PublicRegistration", description = "Публичные данные для формы саморегистрации студента")
public class PublicRegistrationCatalogController {

    private final SpecialityService specialityService;
    private final SkillService skillService;
    private final CompanyService companyService;
    private final EducationService educationService;
    private final RegistrationProperties registrationProperties;

    @Operation(summary = "Список специальностей", description = "Только чтение; размер страницы ограничен конфигурацией")
    @GetMapping("/specialities")
    public ResponseEntity<PageResponse<SpecialityDTO>> listSpecialities(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable p = cappedPageable(page, size);
        return ResponseEntity.ok(specialityService.getAllByFilter(p, new FilterSpecialityReq(null)));
    }

    @Operation(summary = "Список навыков", description = "Только чтение; размер страницы ограничен конфигурацией")
    @GetMapping("/skills")
    public ResponseEntity<PageResponse<SkillDTO>> listSkills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable p = cappedPageable(page, size);
        return ResponseEntity.ok(skillService.getAllByFilter(p, new FilterSkillReq(null)));
    }

    @Operation(summary = "Список компаний", description = "Для выбора места работы; при отсутствии — своё название в теле регистрации")
    @GetMapping("/companies")
    public ResponseEntity<PageResponse<CompanyDTO>> listCompanies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable p = cappedPageable(page, size);
        return ResponseEntity.ok(companyService.getAllByFilter(p, new FilterCompanyReq(null)));
    }

    @Operation(summary = "Список записей образования", description = "Справочник учебных заведений; при отсутствии — своё название + webUrl в теле регистрации")
    @GetMapping("/educations")
    public ResponseEntity<PageResponse<EducationDTO>> listEducations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable p = cappedPageable(page, size);
        return ResponseEntity.ok(educationService.getAllByFilter(p, new FilterEducationReq(null, null, null, null)));
    }

    private Pageable cappedPageable(int page, int size) {
        int cap = Math.max(1, registrationProperties.getMaxCatalogPageSize());
        int s = Math.min(Math.max(1, size), cap);
        return PageRequest.of(Math.max(0, page), s);
    }
}
