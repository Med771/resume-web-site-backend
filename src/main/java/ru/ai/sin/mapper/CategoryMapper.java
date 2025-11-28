package ru.ai.sin.mapper;

import org.mapstruct.*;

import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.CategoryDTO;
import ru.ai.sin.entity.CategoryEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    // ---------------- AddCategoryReq -> CategoryEnt ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    CategoryEnt toEntity(AddCategoryReq dto);

    // ---------------- CategoryEnt -> CategoryDTO ----------------
    @Mapping(target = "skills", ignore = true)
    CategoryDTO toDto(CategoryEnt entity);
}
