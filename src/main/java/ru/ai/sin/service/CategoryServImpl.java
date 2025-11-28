package ru.ai.sin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.CategoryDTO;
import ru.ai.sin.dto.category.SetCategoryNameReq;
import ru.ai.sin.dto.category.SetCategorySkillsReq;
import ru.ai.sin.repository.CategoryRepo;
import ru.ai.sin.service.impl.CategoryService;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor()
public class CategoryServImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Override
    public CategoryDTO getById(Long id) {
        return null;
    }

    @Override
    public List<CategoryDTO> getAll(long page, long size) {
        return List.of();
    }

    @Override
    public CategoryDTO create(AddCategoryReq addCategoryReq) {
        return null;
    }

    @Override
    public CategoryDTO setNameById(Long id, SetCategoryNameReq setCategoryNameReq) {
        return null;
    }

    @Override
    public CategoryDTO setSkillsById(Long id, SetCategorySkillsReq setCategorySkillsReq) {
        return null;
    }

    @Override
    public CategoryDTO deleteById(Long id) {
        return null;
    }
}
