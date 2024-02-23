package de.whosfritz.railinsights.data.repositories;

import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Line}
 */
@Repository
public interface LineRepository extends ListCrudRepository<Line, Long> {

    Optional<Line> findByLineId(String lineId);

    // find Lines By Product and Group by productname
    Optional<List<Line>> findLinesByProduct(String product);

    Optional<List<Line>> findLinesByProductName(String productName);

    Optional<Line> findByLineIdAndFahrtNr(String lineId, String fahrtNr);

}