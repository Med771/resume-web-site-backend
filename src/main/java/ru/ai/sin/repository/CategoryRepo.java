package ru.ai.sin.repository;

import org.springframework.data.repository.CrudRepository;
import ru.ai.sin.entity.CategoryEnt;

public interface CategoryRepo extends CrudRepository<CategoryEnt, Long> {
}
