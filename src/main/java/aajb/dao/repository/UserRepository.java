package aajb.dao.repository;

import aajb.domain.user.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ayed.h on 22/02/2016.
 */
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u from User u where u.login=?1")
    User findByLogin(String login);

    @Query("SELECT u from User u where u.email=?1")
    User findByEmail(String email);
}
