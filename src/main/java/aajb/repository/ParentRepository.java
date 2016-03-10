package aajb.repository;

import aajb.domain.school.Parent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by ayed.h on 23/02/2016.
 */
public interface ParentRepository extends PagingAndSortingRepository<Parent,Integer> {
    Optional<Parent> findByEmail(String email);
    List<Parent> findByFirstNameLikeOrLastNameLike(String firstName,String lastName);
}
