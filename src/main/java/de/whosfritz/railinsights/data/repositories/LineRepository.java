package de.whosfritz.railinsights.data.repositories;

import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Line}
 */
@Repository
public interface LineRepository extends ListCrudRepository<Line, Long> {

    Optional<Line> findByLineIdAndFahrtNr(String lineId, String fahrtNr);

    @Query("SELECT l FROM Line l WHERE l.product IN :products order by l.productName asc")
    Optional<List<Line>> findLinesByProducts(@Param("products") List<String> products);

}