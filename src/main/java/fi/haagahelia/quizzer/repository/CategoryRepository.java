package fi.haagahelia.quizzer.repository;

import org.springframework.stereotype.Repository;

import fi.haagahelia.quizzer.model.Category;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByName(String name);

    List<Category> findAllByOrderByNameAsc();
}
