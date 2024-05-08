package fi.haagahelia.quizzer.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import fi.haagahelia.quizzer.model.AppUser;


public interface AppUserRepository extends JpaRepository <AppUser,Long> {
    AppUser findByUserName(String userName);
}
