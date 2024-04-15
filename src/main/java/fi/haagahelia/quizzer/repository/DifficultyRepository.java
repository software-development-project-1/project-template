package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Difficulty;

import java.util.List;

public interface DifficultyRepository extends CrudRepository<Difficulty, Long> {
    List<Difficulty> findByDifficulty(String difficulty);

}
