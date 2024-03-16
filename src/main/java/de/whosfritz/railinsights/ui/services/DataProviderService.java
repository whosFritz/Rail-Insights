package de.whosfritz.railinsights.ui.services;

import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railinsights.data.dto.StopDto;
import de.whosfritz.railinsights.data.dto.TripPercentageDTO;
import de.whosfritz.railinsights.data.repositories.stop_repositories.StopRepository;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import de.whosfritz.railinsights.data.repositories.trip_repositories.sub.RemarkRepository;
import de.whosfritz.railinsights.data.services.trip_services.TripService;
import de.whosfritz.railinsights.exception.JPAError;
import de.whosfritz.railinsights.ui.color_scheme.ColorScheme;
import de.whosfritz.railinsights.utils.PercentageUtil;
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

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@Setter
@Log4j2
@EnableAsync
@EnableScheduling
public class DataProviderService {

    DataProviderServiceState state = DataProviderServiceState.PENDING;

    List<Stop> longDistanceStops = new ArrayList<>();

    Double stopsPercentageOnTime;

    Double stopsPercentageDelayed;

    Double stopsPercentageCancelled;

    Double stopsPercentageDelayedMoreThan6min;

    Double stopsPercentageDelayedMoreThan15min;

    Double stopsPercentageDelayedMoreThan30min;

    Double stopsPercentageDelayedMoreThan60min;

    DataSeries stoppsOverTimeDataSeries;

    DataSeries percentageOnTimeDataSeries;

    DataSeries percentageCancelledDataSeries;

    DataSeries percentageDelayedDataSeries;

    int totalTrips;

    int totalStops;

    int totalTripsToday;

    int nationalStops;

    List<Remark> top10RemarksFromToday;

    @Autowired
    private TripsRepository tripsRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private RemarkRepository remarkRepository;

    @Autowired
    private TripService tripService;

    public DataProviderService() {
    }

    /**
     * Run the data calculation every 20 minutes.
     */
    @Scheduled(cron = "0 0/20 * * * ?") // every 20 minutes
    @Async
    @Transactional
    public void calculateData() {
        state = DataProviderServiceState.PENDING; // Set the state to pending
        log.info("Data calculation started...");
        longDistanceStops = stopRepository.findByProducts_NationalOrAndProductsNationalExpress(true, true);
        totalStops = (int) stopRepository.count();
        totalTrips = (int) tripsRepository.count();

        List<Object[]> tripsByDate = tripsRepository.countTripsByDate();
        Map<LocalDate, Integer> dailyTripCounts = new HashMap<>();

        for (Object[] trip : tripsByDate) {
            Date sqlDate = (Date) trip[0];
            LocalDate tripDate = sqlDate.toLocalDate();
            int tripCount = ((Number) trip[1]).intValue();
            dailyTripCounts.put(tripDate, tripCount);
        }
        // order the map by localDate
        dailyTripCounts = dailyTripCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, HashMap::new));

        int countAllByCancelled = tripsRepository.countAllByCancelled(true);
        int countAllByDelayIsGreaterThanEqual = tripsRepository.countAllByDelayIsGreaterThanEqual(360);
        int countOnTime = totalTrips - countAllByCancelled - countAllByDelayIsGreaterThanEqual;
        int countDelayedMoreThan15min = tripsRepository.countAllByDelayIsGreaterThanEqual(900);
        int countDelayedMoreThan30min = tripsRepository.countAllByDelayIsGreaterThanEqual(1800);
        int countDelayedMoreThan60min = tripsRepository.countAllByDelayIsGreaterThanEqual(3600);

        stopsPercentageOnTime = PercentageUtil.convertToTwoDecimalPlaces((double) countOnTime / totalTrips * 100);
        stopsPercentageDelayed = PercentageUtil.convertToTwoDecimalPlaces((double) countAllByDelayIsGreaterThanEqual / totalTrips * 100);
        stopsPercentageCancelled = PercentageUtil.convertToTwoDecimalPlaces((double) countAllByCancelled / totalTrips * 100);
        stopsPercentageDelayedMoreThan6min = stopsPercentageDelayed;
        stopsPercentageDelayedMoreThan15min = PercentageUtil.convertToTwoDecimalPlaces((double) countDelayedMoreThan15min / totalTrips * 100);
        stopsPercentageDelayedMoreThan30min = PercentageUtil.convertToTwoDecimalPlaces((double) countDelayedMoreThan30min / totalTrips * 100);
        stopsPercentageDelayedMoreThan60min = Math.round((double) countDelayedMoreThan60min / totalTrips * 1000.0) / 1000.0 * 100;

        DataSeries dataSeries = new DataSeries();
        dataSeries.setName("Stopps");
        for (Map.Entry<LocalDate, Integer> entry : dailyTripCounts.entrySet()) {
            DataSeriesItem dataSeriesItem = new DataSeriesItem(entry.getKey().atStartOfDay(ZoneId.systemDefault()).toInstant(), entry.getValue());
            dataSeriesItem.setColor(new SolidColor(ColorScheme.INFO_LIGHT.getColor()));
            dataSeries.add(dataSeriesItem);
        }
        stoppsOverTimeDataSeries = dataSeries;

        totalTripsToday = dailyTripCounts.getOrDefault(LocalDate.now(), 0);
        nationalStops = longDistanceStops.size();

        top10RemarksFromToday = remarkRepository.findTop10RemarksFromToday();

        Result<List<TripPercentageDTO>, JPAError> tripPercentageDTOS = tripService.getPercentages();
        if (tripPercentageDTOS.isSuccess()) {
            // order after tripDate
            tripPercentageDTOS.getData().sort(Comparator.comparing(TripPercentageDTO::getTripDate));
            // create the DataSeries containing the percentage values for each day
            DataSeries onTimeSeries = new DataSeries();
            onTimeSeries.setName("Pünktlichkeit");

            DataSeries delayedSeries = new DataSeries();
            delayedSeries.setName("Verspätung");

            DataSeries cancelledSeries = new DataSeries();
            cancelledSeries.setName("Ausfall");
            for (TripPercentageDTO tripPercentageDTO : tripPercentageDTOS.getData()) {
                Instant instant = tripPercentageDTO.getTripDate().atStartOfDay(ZoneId.systemDefault()).toInstant();
                DataSeriesItem dataSeriesItem = new DataSeriesItem(instant, tripPercentageDTO.getOnTimePercentage());
                dataSeriesItem.setColor(new SolidColor(ColorScheme.SUCCESS.getColor()));
                onTimeSeries.add(dataSeriesItem);

                DataSeriesItem dataSeriesItemDelayed = new DataSeriesItem(instant, tripPercentageDTO.getDelayedPercentage());
                dataSeriesItemDelayed.setColor(new SolidColor(ColorScheme.WARNING_SOFT.getColor()));
                delayedSeries.add(dataSeriesItemDelayed);

                DataSeriesItem dataSeriesItemCancelled = new DataSeriesItem(instant, tripPercentageDTO.getCancelledPercentage());
                dataSeriesItemCancelled.setColor(new SolidColor(ColorScheme.ERROR.getColor()));
                cancelledSeries.add(dataSeriesItemCancelled);
            }

            percentageOnTimeDataSeries = onTimeSeries;
            percentageDelayedDataSeries = delayedSeries;
            percentageCancelledDataSeries = cancelledSeries;
        }

        log.info("Data calculation finished...");
        state = DataProviderServiceState.READY; // Set the state to ready
    }

    /**
     * Get all national stops as DTOs.
     *
     * @return the converted stopDTOs list
     */
    public List<StopDto> getAllNationalStopsConvertedToDto() {
        return longDistanceStops.parallelStream()
                .map(stop -> new StopDto(
                        stop.getStopId().toString(),
                        stop.getName(),
                        stop.getLocation().getLatitude(),
                        stop.getLocation().getLongitude(),
                        stop.getStation())).toList();
    }

    public Remark getRandomRemark() {
        if (top10RemarksFromToday == null || top10RemarksFromToday.isEmpty()) {
            Remark remark = new Remark();
            remark.setText("Keine Meldungen vorhanden.");
            return remark;
        }
        int randomIndex = (int) (Math.random() * top10RemarksFromToday.size());
        return top10RemarksFromToday.get(randomIndex);
    }

}
