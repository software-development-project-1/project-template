package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import ch.qos.logback.core.status.Status;
import fi.haagahelia.quizzer.model.Quizz;

import java.util.List;

public interface QuizzRepository extends CrudRepository<Quizz, Long> {
    List<Quizz> findByStatus(Status status);

    List<Quizz> findAll();

  
}
