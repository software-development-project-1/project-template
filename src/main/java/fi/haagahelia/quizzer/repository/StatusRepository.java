package fi.haagahelia.quizzer.repository;

import org.springframework.data.repository.CrudRepository;

import fi.haagahelia.quizzer.model.Status;

import java.util.List;


public interface StatusRepository extends CrudRepository<Status,Long>{
    List<Status> findByStatus(Boolean status);

}
