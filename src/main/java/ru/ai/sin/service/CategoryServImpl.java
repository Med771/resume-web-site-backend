package ru.ai.sin.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.ai.sin.dto.category.AddCategoryReq;
import ru.ai.sin.dto.category.CategoryDTO;
import ru.ai.sin.dto.category.SetCategoryNameReq;
import ru.ai.sin.dto.category.SetCategorySkillsReq;
import ru.ai.sin.dto.skill.SkillDTO;
import ru.ai.sin.entity.CategoryEnt;
import ru.ai.sin.entity.SkillEnt;
import ru.ai.sin.exception.models.BadRequestException;
import ru.ai.sin.mapper.CategoryMapper;
import ru.ai.sin.mapper.SkillMapper;
import ru.ai.sin.repository.CategoryRepo;
import ru.ai.sin.repository.SkillRepo;
import ru.ai.sin.service.impl.CategoryService;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor()
public class CategoryServImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    private final SkillRepo skillRepo;

    private final CategoryMapper categoryMapper;
    private final SkillMapper skillMapper;

    @Override
    public CategoryDTO getById(Long id) {
        CategoryEnt categoryEnt = categoryRepo.findByIdAndIsActiveTrue(id).orElseThrow(
                () -> new BadRequestException("Failed find by id")
        );

        List<SkillDTO> skillDTOs = categoryEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return categoryMapper.toDto(categoryEnt, skillDTOs);
    }

    @Override
    public List<CategoryDTO> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);

        List<CategoryEnt> list = categoryRepo.findAllByIsActiveTrue(pageRequest);

        return list.stream()
                .map(categoryEnt -> {
                    List<SkillDTO> skillDTOs = categoryEnt.getSkills().stream()
                            .map(skillMapper::toDTO)
                            .toList();

                    return categoryMapper.toDto(categoryEnt, skillDTOs);
                })
                .toList();
    }

    @Override
    @Transactional
    public CategoryDTO create(AddCategoryReq addCategoryReq) {
        CategoryEnt categoryEnt;

        Optional<CategoryEnt> optCategoryEnt = categoryRepo.findByNameIgnoreCase(addCategoryReq.name());

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(addCategoryReq.skillsIds());
        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();

        if (optCategoryEnt.isPresent()) {
            categoryEnt = optCategoryEnt.get();

            categoryEnt.setIsActive(true);
        }
        else {
            categoryEnt = categoryMapper.toEntity(addCategoryReq);
        }

        categoryEnt.setSkills(skillEntSet);

        categoryEnt = categoryRepo.save(categoryEnt);

        return categoryMapper.toDto(categoryEnt, skillDTOs);
    }

    @Override
    @Transactional
    public CategoryDTO setNameById(Long id, SetCategoryNameReq setCategoryNameReq) {
        CategoryEnt categoryEnt = categoryRepo.findByIdAndIsActiveTrue(id).orElseThrow(
                () -> new BadRequestException("Failed find by id")
        );

        categoryEnt.setName(setCategoryNameReq.name());

        categoryEnt = categoryRepo.save(categoryEnt);

        List<SkillDTO> skillDTOs = categoryEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return categoryMapper.toDto(categoryEnt, skillDTOs);
    }

    @Override
    @Transactional
    public CategoryDTO setSkillsById(Long id, SetCategorySkillsReq setCategorySkillsReq) {
        CategoryEnt categoryEnt = categoryRepo.findByIdAndIsActiveTrue(id).orElseThrow(
                () -> new BadRequestException("Failed find by id")
        );

        Set<SkillEnt> skillEntSet = skillRepo.findAllByIdIn(setCategorySkillsReq.skillsIds());
        List<SkillDTO> skillDTOs = skillEntSet.stream().map(skillMapper::toDTO).toList();

        categoryEnt.setSkills(skillEntSet);

        categoryEnt = categoryRepo.save(categoryEnt);

        return categoryMapper.toDto(categoryEnt, skillDTOs);
    }

    @Override
    @Transactional
    public CategoryDTO deleteById(Long id) {
        CategoryEnt categoryEnt = categoryRepo.findByIdAndIsActiveTrue(id).orElseThrow(
                () -> new BadRequestException("Failed find by id")
        );

        categoryEnt.setIsActive(false);

        categoryEnt = categoryRepo.save(categoryEnt);

        List<SkillDTO> skillDTOs = categoryEnt.getSkills().stream().map(skillMapper::toDTO).toList();

        return categoryMapper.toDto(categoryEnt, skillDTOs);
    }
}
