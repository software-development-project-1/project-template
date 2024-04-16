package fi.haagahelia.quizzer.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import fi.haagahelia.quizzer.model.Quizz;

import java.util.List;
import java.time.Instant;

public interface QuizzRepository extends CrudRepository<Quizz, Long> {
    List<Quizz> findByName(String name);

    // Add custom query method to find quizzes created after the specified date
    @Query("SELECT q FROM Quiz q WHERE q.creationDate > :date")
    List<Quizz> findQuizzesByCreationDateAfter(@Param("date") Instant date);
}
