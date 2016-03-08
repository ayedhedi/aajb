package aajb.repository;

import aajb.domain.audit.Operation;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by ayed.h on 01/03/2016.
 */
public interface OperationRepository extends CrudRepository<Operation,Long> {
}
