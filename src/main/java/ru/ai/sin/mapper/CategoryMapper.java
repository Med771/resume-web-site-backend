package ru.ai.sin.mapper;

import org.mapstruct.*;

import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.GetCategoryRes;
import ru.ai.sin.dto.category.MergeCategoryReq;
import ru.ai.sin.entity.CategoryEnt;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    // ---------------- AddCategoryReq -> Entity ----------------
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    CategoryEnt toEntity(AddCategoryReq dto);

    // ---------------- Entity -> GetCategoryRes ----------------
    @Mapping(source = "timestamps.createdAt", target = "createdAt")
    @Mapping(source = "timestamps.updatedAt", target = "updatedAt")
    GetCategoryRes toDto(CategoryEnt entity);

    // ---------------- MergeCategoryReq -> Entity ----------------
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "timestamps", ignore = true)
    @Mapping(target = "skills", ignore = true)
    void updateEntityFromDto(MergeCategoryReq dto, @MappingTarget CategoryEnt entity);
}
