package de.whosfritz.railmetrics.data.repositories;

import de.olech2412.adapter.dbadapter.model.station.sub.Operator;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Operator}.
 */
@Repository
public interface OperatorRepository extends ListCrudRepository<Operator, Long> {

    Optional<Operator> findByOperatorId(String operatorId);

}