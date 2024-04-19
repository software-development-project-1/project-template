package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Status;

public interface StatusRepository extends CrudRepository<Status, Long> {
    Status findByStatus(Boolean status);

}
