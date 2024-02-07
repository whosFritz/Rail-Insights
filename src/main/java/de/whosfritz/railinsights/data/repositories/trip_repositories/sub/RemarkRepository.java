package de.whosfritz.railinsights.data.repositories.trip_repositories.sub;

import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for {@link Remark}
 */
@Repository
public interface RemarkRepository extends ListCrudRepository<Remark, Long> {

    Optional<Remark> findByText(String text);

}