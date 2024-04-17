package fi.haagahelia.quizzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haagahelia.quizzer.model.Quizz;

import java.util.List;
import java.time.Instant;

public interface QuizzRepository extends JpaRepository<Quizz, Long> {
    List<Quizz> findByCreationTimeAfter(Instant creationTime);
}
