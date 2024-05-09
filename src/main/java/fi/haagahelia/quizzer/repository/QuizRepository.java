package fi.haagahelia.quizzer.repository;

import org.springframework.stereotype.Repository;
import java.util.List;
import fi.haagahelia.quizzer.model.Quiz;
import fi.haagahelia.quizzer.model.AppUser;
import fi.haagahelia.quizzer.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>{
    List<Quiz> findByUser(AppUser user);
    List<Quiz> findByUserOrderByCreatedAtDesc(AppUser user);
    List<Quiz> findAllByOrderByQuizNameAsc();
    List<Quiz> findByUserOrderByCreatedAtAsc(AppUser user);
    List<Quiz> findByPublished(boolean published);
    List<Quiz> findByUserOrderByQuizNameAsc(AppUser user);
    List<Quiz> findByPublishedOrderByCreatedAtDesc(boolean published);
    List<Quiz> findByPublishedOrderByCreatedAtAsc(boolean published);
    List<Quiz> findAllByCategoryId(Long categoryId);
    List<Quiz> findAll();
    List<Quiz> findAllByOrderByCreatedAtDesc();
    List<Quiz> findAllByOrderByCreatedAtAsc();

    List<Quiz>  findByUserAndPublished(AppUser user, boolean published);

   

}
