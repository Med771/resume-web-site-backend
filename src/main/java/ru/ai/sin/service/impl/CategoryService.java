package ru.ai.sin.service.impl;

import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.CategoryDTO;
import ru.ai.sin.dto.category.SetCategoryNameReq;
import ru.ai.sin.dto.category.SetCategorySkillsReq;

import java.util.List;

public interface CategoryService {

    // ---------- GET METHODS ----------
    CategoryDTO getById(
            Long id);

    List<CategoryDTO> getAll(
            int page, int size);

    // ---------- POST METHODS ----------
    CategoryDTO create(
            AddCategoryReq addCategoryReq);

    CategoryDTO setNameById(
            long id,
            SetCategoryNameReq setCategoryNameReq);
    CategoryDTO setSkillsById(
            long id,
            SetCategorySkillsReq setCategorySkillsReq);

    // ---------- POST METHODS ----------
    CategoryDTO deleteById(Long id);
}
