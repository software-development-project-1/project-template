package fi.haagahelia.quizzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haagahelia.quizzer.model.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    // Find Reviews by Quiz ID:
    List<Review> findByQuizId(Long quizId);
    

}
