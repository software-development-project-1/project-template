package fi.haagahelia.quizzer.repository;

import org.springframework.stereotype.Repository;

import fi.haagahelia.quizzer.model.Category;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
