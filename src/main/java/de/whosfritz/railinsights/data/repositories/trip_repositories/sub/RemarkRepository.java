package de.whosfritz.railinsights.data.repositories.trip_repositories.sub;

import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for {@link Remark}
 */
@Repository
public interface RemarkRepository extends ListCrudRepository<Remark, Long> {

    Optional<Remark> findByText(String text);

    @Query(value = "SELECT r.id, r.remark_code, r.created_at, r.remark_text, r.remark_type\n" +
            "FROM remark r\n" +
            "JOIN trip_remark tr ON r.id = tr.remark_id\n" +
            "JOIN trips t ON tr.trip_id = t.id\n" +
            "WHERE DATE(t.trip_planned_when) = CURDATE()\n" +
            "AND (r.remark_code IS NULL OR r.remark_code NOT IN ('cancelled', 'alternative-trip'))\n" +
            "GROUP BY r.id, r.remark_code, r.created_at, r.remark_text, r.remark_type\n" +
            "ORDER BY COUNT(tr.trip_id) DESC\n" +
            "LIMIT 10;", nativeQuery = true)
    List<Remark> findTop10RemarksFromToday();

}