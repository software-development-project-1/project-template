package fi.haagahelia.quizzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    List<Review> findByQuizId(Long quizId);
    List<Review> findByQuiz(Quiz quiz);
    

}
