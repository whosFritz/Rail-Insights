package de.whosfritz.railinsights.ui.services;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.dto.StopDto;
import de.whosfritz.railinsights.data.repositories.stop_repositories.StopRepository;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import de.whosfritz.railinsights.ui.color_scheme.ColorScheme;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Getter
@Setter
@Log4j2
@EnableAsync
@EnableScheduling
public class DataProviderService {

    DataProviderServiceState state = DataProviderServiceState.PENDING;

    List<Stop> allStops = new ArrayList<>();

    Double stopsPercentageOnTime;

    Double stopsPercentageDelayed;

    Double stopsPercentageCancelled;

    Double stopsPercentageDelayedMoreThan6min;

    Double stopsPercentageDelayedMoreThan15min;

    Double stopsPercentageDelayedMoreThan60min;

    DataSeries stoppsOverTimeDataSeries;

    int totalTrips;

    int totalStops;

    int totalTripsToday;

    int nationalStops;

    @Autowired
    private TripsRepository tripsRepository;
    @Autowired
    private StopRepository stopRepository;

    public DataProviderService() {
    }

    /**
     * Run the data calculation every 20 minutes.
     */
    @Scheduled(cron = "0 0/20 * * * ?")
    @Async
    @Transactional
    public void calculateData() {
        state = DataProviderServiceState.PENDING; // Set the state to pending
        log.info("Data calculation started...");
        allStops = stopRepository.findAll();
        List<Trip> allTrips = tripsRepository.findAll();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setGroupingUsed(false);

        double percentageCancelled = ((double) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() != null) {
                return trip.getCancelled();
            }
            return false;
        }).count() / allTrips.size());

        stopsPercentageCancelled = (Math.round(percentageCancelled * 100.0) / 100.0) * 100;
        String formatted = df.format(stopsPercentageCancelled);
        stopsPercentageCancelled = Double.parseDouble(formatted);

        double percentageDelayed = ((double) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() == null || !trip.getCancelled()) {
                if (trip.getDelay() != null) {
                    return trip.getDelay() >= 360;
                }
            }
            return false;
        }).count() / allTrips.size());

        stopsPercentageDelayed = (Math.round(percentageDelayed * 100.0) / 100.0) * 100;
        formatted = df.format(stopsPercentageDelayed);
        stopsPercentageDelayed = Double.parseDouble(formatted);

        double percentageOnTime = (1 - percentageCancelled - percentageDelayed);

        stopsPercentageOnTime = (Math.round(percentageOnTime * 100.0) / 100.0) * 100;
        formatted = df.format(stopsPercentageOnTime);
        stopsPercentageOnTime = Double.parseDouble(formatted);

        stopsPercentageDelayedMoreThan6min = stopsPercentageDelayed;

        double percentageDelayedMoreThan15min = ((double) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() == null || !trip.getCancelled()) {
                if (trip.getDelay() != null) {
                    return trip.getDelay() >= 900;
                }
            }
            return false;
        }).count() / allTrips.size());

        stopsPercentageDelayedMoreThan15min = (Math.round(percentageDelayedMoreThan15min * 100.0) / 100.0) * 100;
        formatted = df.format(stopsPercentageDelayedMoreThan15min);
        stopsPercentageDelayedMoreThan15min = Double.parseDouble(formatted);

        double percentageDelayedMoreThan60min = ((double) allTrips.stream().filter(trip -> {
            if (trip.getCancelled() == null || !trip.getCancelled()) {
                if (trip.getDelay() != null) {
                    return trip.getDelay() >= 3600;
                }
            }
            return false;
        }).count());
        double stopsPercentageDelayedMoreThan60mi = percentageDelayedMoreThan60min / allTrips.size();
        stopsPercentageDelayedMoreThan60min = Math.round(stopsPercentageDelayedMoreThan60mi * 1000.0) / 1000.0 * 100;

        generateHomeViewStatistics(allTrips);

        totalTrips = allTrips.size();
        totalStops = allStops.size();
        totalTripsToday = (int) allTrips.stream().filter(trip -> trip.getCreatedAt().toLocalDate().equals(LocalDate.now())).count();
        nationalStops = (int) allStops.stream().filter(stop -> stop.getProducts().isNational()).count();

        log.info("Data calculation finished...");
        state = DataProviderServiceState.READY; // Set the state to ready
    }

    private void generateHomeViewStatistics(List<Trip> allTrips) {
        // HashMap zur Speicherung der Summe der Trips pro Tag
        Map<LocalDate, Integer> dailyTripCounts = new HashMap<>();

        // Iteration durch alle Trips
        for (Trip trip : allTrips) {
            LocalDate tripDate;
            // Wenn trip.getWhen() vorhanden ist, verwenden wir dieses Datum, ansonsten trip.getPlannedWhen() oder trip.getCreatedAt()
            if (trip.getWhen() != null) {
                tripDate = trip.getWhen().toLocalDate();
            } else if (trip.getPlannedWhen() != null) {
                tripDate = trip.getPlannedWhen().toLocalDate();
            } else {
                tripDate = trip.getCreatedAt().toLocalDate();
            }

            // Aktuellen Wert für den Tag holen und um 1 erhöhen
            dailyTripCounts.put(tripDate, dailyTripCounts.getOrDefault(tripDate, 0) + 1);
        }

        DataSeries dataSeries = new DataSeries();
        dataSeries.setName("Stopps");
        for (Map.Entry<LocalDate, Integer> entry : dailyTripCounts.entrySet()) {
            DataSeriesItem dataSeriesItem = new DataSeriesItem(entry.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant(), entry.getValue());
            dataSeriesItem.setColor(new SolidColor(ColorScheme.INFO_LIGHT.getColor()));
            dataSeries.add(dataSeriesItem);
        }
        stoppsOverTimeDataSeries = dataSeries;
    }

    /**
     * Get all national stops as DTOs.
     *
     * @return the converted stopDTOs list
     */
    public List<StopDto> getAllNationalStopsConvertedToDto() {
        List<StopDto> stopDtos = new ArrayList<>();
        allStops.stream().filter(stop -> stop.getProducts().isNational()).toList().forEach(stop ->
                stopDtos.add(new StopDto(stop.getStopId().toString(), stop.getName(), stop.getLocation().getLatitude(), stop.getLocation().getLongitude(), stop.getStation())));
        return stopDtos;
    }

}
