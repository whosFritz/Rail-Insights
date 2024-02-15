package de.whosfritz.railinsights.data.dataprovider;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.data.provider.SortDirection;
import com.vaadin.flow.server.VaadinService;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.services.trip_services.TripService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class TripDataProvider extends AbstractBackEndDataProvider<Trip, TripFilter> {

    TripService tripService;

    Stop currentStop;

    LocalDateTime whenAfter;

    LocalDateTime whenBefore;

    public TripDataProvider(Stop stop, LocalDateTime whenAfter, LocalDateTime whenBefore) {
        currentStop = stop;
        this.whenAfter = whenAfter;
        this.whenBefore = whenBefore;
        tripService = VaadinService.getCurrent().getInstantiator().getOrCreate(TripService.class);
    }

    private static Comparator<Trip> sortComparator(List<QuerySortOrder> sortOrders) {
        return sortOrders.stream().map(sortOrder -> {
            Comparator<Trip> comparator = tripFieldComparator(sortOrder.getSorted());

            if (sortOrder.getDirection() == SortDirection.DESCENDING) {
                comparator = comparator.reversed();
            }

            return comparator;
        }).reduce(Comparator::thenComparing).orElse((p1, p2) -> 0);
    }

    private static Comparator<Trip> tripFieldComparator(String sorted) {
        if (sorted.equals("line")) {
            return Comparator.comparing(trip -> trip.getLine().getName());
        } else if (sorted.equals("direction")) {
            return Comparator.comparing(trip -> trip.getDirection());
        }
        return (p1, p2) -> 0;
    }

    public void updateWhenAfterAndWhenBefore(LocalDateTime whenAfter, LocalDateTime whenBefore) {
        this.whenAfter = whenAfter;
        this.whenBefore = whenBefore;
    }

    @Override
    protected Stream<Trip> fetchFromBackEnd(Query<Trip, TripFilter> query) {
        Stream<Trip> stream = tripService.findAllByStopAndPlannedWhenAfterAndWhenBefore(currentStop, whenAfter, whenBefore).getData().stream();

        // Filtering
        if (query.getFilter().isPresent()) {
            stream = stream.filter(trip -> query.getFilter().get().test(trip));
        }

        // Sorting
        if (!query.getSortOrders().isEmpty()) {
            stream = stream.sorted(sortComparator(query.getSortOrders()));
        }

        // Pagination
        return stream.skip(query.getOffset()).limit(query.getLimit());
    }

    @Override
    protected int sizeInBackEnd(Query<Trip, TripFilter> query) {
        return (int) fetchFromBackEnd(query).count();
    }
}
