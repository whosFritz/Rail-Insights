package de.whosfritz.railinsights.data.repositories;

import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Line}
 */
@Repository
public interface LineRepository extends ListCrudRepository<Line, Long> {

    Optional<Line> findByLineId(String lineId);

    Optional<Line> findByLineIdAndFahrtNr(String lineId, String fahrtNr);

}