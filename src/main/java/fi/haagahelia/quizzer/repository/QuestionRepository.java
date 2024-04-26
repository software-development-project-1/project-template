package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Question;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, Long> {
    List<Question> findByQuestionId(Long quizzId);
}
