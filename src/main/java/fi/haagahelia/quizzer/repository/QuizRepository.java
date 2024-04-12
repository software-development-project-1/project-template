package fi.haagahelia.quizzer.repository;

import org.springframework.stereotype.Repository;

import fi.haagahelia.quizzer.model.Quiz;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long>{
    
}
