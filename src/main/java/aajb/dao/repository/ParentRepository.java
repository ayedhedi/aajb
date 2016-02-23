package aajb.dao.repository;

import aajb.domain.school.Parent;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ayed.h on 23/02/2016.
 */
public interface ParentRepository extends CrudRepository<Parent,Integer> {
    Parent findByLogin(String login);
    Parent findByEmail(String email);
}
