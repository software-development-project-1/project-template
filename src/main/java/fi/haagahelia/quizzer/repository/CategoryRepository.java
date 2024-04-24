package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Category;

import java.util.List;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    List<Category> findByName(String name);

    List<Category> findCategoryById(Long categoryId);

}
