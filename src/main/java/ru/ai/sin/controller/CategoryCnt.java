package ru.ai.sin.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.CategoryDTO;
import ru.ai.sin.dto.category.SetCategoryNameReq;
import ru.ai.sin.dto.category.SetCategorySkillsReq;
import ru.ai.sin.service.impl.CategoryService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/category")
public class CategoryCnt {

    private final CategoryService categoryService;

    @GetMapping(path = "/getById")
    public ResponseEntity<CategoryDTO> getById(
            @RequestParam long id) {
        CategoryDTO categoryDTO = categoryService.getById(id);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }

    @GetMapping(path = "/getAll")
    public ResponseEntity<List<CategoryDTO>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        List<CategoryDTO> categoryDTOs = categoryService.getAll(page, size);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTOs);
    }

    @PostMapping(path = "/create")
    public ResponseEntity<CategoryDTO> create(
            @RequestBody AddCategoryReq categoryReq) {
        CategoryDTO categoryDTO = categoryService.create(categoryReq);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDTO);
    }

    @PutMapping(path = "/setNameById")
    public ResponseEntity<CategoryDTO> setNameById(
            @RequestParam long id,
            @RequestBody SetCategoryNameReq  setCategoryNameReq) {
        CategoryDTO categoryDTO = categoryService.setNameById(id, setCategoryNameReq);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }

    @PutMapping(path = "/setSkillsById")
    public ResponseEntity<CategoryDTO> setSkillsById(
            @RequestParam long id,
            @RequestBody SetCategorySkillsReq setCategorySkillsReq) {
        CategoryDTO categoryDTO = categoryService.setSkillsById(id, setCategorySkillsReq);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }

    @DeleteMapping(path = "/deleteById")
    public ResponseEntity<CategoryDTO> deleteById(
            @RequestParam long id) {
        CategoryDTO categoryDTO = categoryService.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).body(categoryDTO);
    }
}
