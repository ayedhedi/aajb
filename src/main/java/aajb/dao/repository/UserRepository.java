package aajb.dao.repository;

import aajb.domain.user.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ayed.h on 22/02/2016.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByLogin(String login);
    User findByEmail(String email);
}
