package de.whosfritz.railmetrics.data.repositories.station_repositories.sub;

import de.olech2412.adapter.dbadapter.model.station.sub.ProductLine;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link ProductLine}s.
 */
@Repository
public interface ProductLineRepository extends ListCrudRepository<ProductLine, Long> {

    Optional<ProductLine> findByProductLine(String productLine);

}