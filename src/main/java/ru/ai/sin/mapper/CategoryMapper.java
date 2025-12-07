package ru.ai.sin.mapper;

import org.mapstruct.*;

import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.CategoryDTO;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.CategoryEnt;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    // ---------------- AddCategoryReq -> CategoryEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    CategoryEnt toEntity(AddCategoryReq dto);

    // ---------------- CategoryEnt -> CategoryDTO ----------------
    CategoryDTO toDto(CategoryEnt entity, List<SkillDTO> skills);
}
