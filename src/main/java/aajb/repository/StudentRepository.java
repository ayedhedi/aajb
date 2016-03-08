package aajb.repository;

import aajb.domain.school.Student;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface StudentRepository extends CrudRepository<Student,Integer> {
}
