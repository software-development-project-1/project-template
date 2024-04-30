package fi.haagahelia.quizzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haagahelia.quizzer.model.Question;
import fi.haagahelia.quizzer.model.Quiz;

public interface QuestionRepository extends JpaRepository <Question, Long> {
    List<Question> findByQuiz(Quiz quiz);
    List<Question> findByQuizId(Long id);
    List<Question> findByQuizAndDifficultyLevel(Quiz quiz, String difficultyLevel);
}
