package aajb.repository;

import aajb.domain.school.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface StudentRepository extends CrudRepository<Student,Integer> {
    @Query(value = "SELECT class_name as cn, count(id) as sum FROM student group by cn", nativeQuery = true)
    List<Object[]> findNumberOfStudentByClass();
}
