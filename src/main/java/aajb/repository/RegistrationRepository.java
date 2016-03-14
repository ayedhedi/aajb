package aajb.repository;

import aajb.domain.school.Registration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface RegistrationRepository extends CrudRepository<Registration,Integer>{

    @Query(value = "select * from registration where first_parent = ?1 or second_parent = ?1", nativeQuery = true)
    List<Registration> findByParentsId(int id);
}
