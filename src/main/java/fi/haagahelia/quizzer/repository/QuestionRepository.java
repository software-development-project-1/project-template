package fi.haagahelia.quizzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haagahelia.quizzer.model.Question;

public interface QuestionRepository extends JpaRepository <Question, Long> {

}
