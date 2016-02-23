package aajb.dao.repository;

import aajb.domain.school.Person;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ayed.h on 22/02/2016.
 */
public interface PersonRepository extends CrudRepository<Person, Integer> {
}
