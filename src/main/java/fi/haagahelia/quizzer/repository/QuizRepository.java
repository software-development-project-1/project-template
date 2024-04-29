package fi.haagahelia.quizzer.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>{
    List<Quiz> findAllByOrderByCreatedAtDesc();
    List<Quiz> findAllByOrderByCreatedAtAsc();
    List<Quiz> findByPublished(boolean published);
    List<Quiz> findAllByOrderByQuizNameAsc();
    List<Quiz> findByPublishedOrderByCreatedAtDesc(boolean published);
    List<Quiz> findAllByCategoryId(Long categoryId);

}
