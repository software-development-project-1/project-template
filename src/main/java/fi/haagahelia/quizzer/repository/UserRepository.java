package fi.haagahelia.quizzer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fi.haagahelia.quizzer.model.User;



public interface UserRepository extends JpaRepository <User,Long> {
    User findByUserName(String userName);
}
