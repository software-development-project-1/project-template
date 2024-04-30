package fi.haagahelia.quizzer.repository;

import org.springframework.stereotype.Repository;
import fi.haagahelia.quizzer.model.Answer;
import fi.haagahelia.quizzer.model.Question;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
     List<Answer> findByQuestionIn(List<Question> questions);
     Optional<Answer> findByQuestion(Question question);
}