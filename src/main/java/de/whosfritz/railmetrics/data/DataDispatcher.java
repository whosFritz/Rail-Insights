package de.whosfritz.railmetrics.data;

import de.olech2412.adapter.dbadapter.APIConfiguration;
import de.olech2412.adapter.dbadapter.DB_Adapter_v6;
import de.olech2412.adapter.dbadapter.exception.Error;
import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.olech2412.adapter.dbadapter.request.parameters.Parameter;
import de.olech2412.adapter.dbadapter.request.parameters.RequestParametersNames;
import de.whosfritz.railmetrics.data.services.stop_services.StopService;
import de.whosfritz.railmetrics.data.services.trip_services.TripService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log4j2
@EnableScheduling
public class DataDispatcher {

    @Autowired
    StopService stopService;

    @Autowired
    TripService tripService;

    /**
     * Fetch all departures for all stops
     */
    @Scheduled(cron = "0 0/15 * * * ?")
    @Async
    public void fetchData() {
        log.info("---- Fetching departures ----");

        List<Stop> stops = stopService.findByProducts_National(true);

        if (!stops.isEmpty()) {
            log.info("Stops found: " + stops.size());

            APIConfiguration apiConfiguration = new APIConfiguration();
            apiConfiguration.setBaseUrl("http://localhost:3000");
            DB_Adapter_v6 db_adapter_v6 = new DB_Adapter_v6(apiConfiguration);

            for (int i = 0; i < stops.size(); i++) {
                try {

                    Result<Trip[], Error> arrivalsByStopId = db_adapter_v6.getArrivalsByStopId(Math.toIntExact(stops.get(i).getStopId()), new Parameter.ParameterBuilder()
                            .add(RequestParametersNames.NATIONAL, true)
                            .add(RequestParametersNames.NATIONAL_EXPRESS, true)
                            .add(RequestParametersNames.REGIONAL, true)
                            .add(RequestParametersNames.REGIONAL_EXPRESS, true)
                            .add(RequestParametersNames.SUBURBAN, true)
                            .add(RequestParametersNames.BUS, false)
                            .add(RequestParametersNames.TRAM, false)
                            .add(RequestParametersNames.FERRY, false)
                            .add(RequestParametersNames.SUBWAY, false)
                            .add(RequestParametersNames.TAXI, false)
                            .add(RequestParametersNames.RESULTS, 9999)
                            .add(RequestParametersNames.DURATION, 60)
                            .build());

                    Result<Trip[], Error> departuresByStopId = db_adapter_v6.getDeparturesByStopId(Math.toIntExact(stops.get(i).getStopId()), new Parameter.ParameterBuilder()
                            .add(RequestParametersNames.NATIONAL, true)
                            .add(RequestParametersNames.NATIONAL_EXPRESS, true)
                            .add(RequestParametersNames.REGIONAL, true)
                            .add(RequestParametersNames.REGIONAL_EXPRESS, true)
                            .add(RequestParametersNames.SUBURBAN, true)
                            .add(RequestParametersNames.BUS, false)
                            .add(RequestParametersNames.TRAM, false)
                            .add(RequestParametersNames.FERRY, false)
                            .add(RequestParametersNames.SUBWAY, false)
                            .add(RequestParametersNames.TAXI, false)
                            .add(RequestParametersNames.RESULTS, 9999)
                            .add(RequestParametersNames.DURATION, 60)
                            .build());

                    if (departuresByStopId.isSuccess() && arrivalsByStopId.isSuccess()) {
                        log.info("Arrivals found: " + arrivalsByStopId.getData().length + " for stop {}", stops.get(i).getName());
                        log.info("Departures found: " + departuresByStopId.getData().length + " for stop {}", stops.get(i).getName());
                        // merge arrays
                        Trip[] trips = new Trip[arrivalsByStopId.getData().length + departuresByStopId.getData().length];
                        System.arraycopy(arrivalsByStopId.getData(), 0, trips, 0, arrivalsByStopId.getData().length);
                        System.arraycopy(departuresByStopId.getData(), 0, trips, arrivalsByStopId.getData().length, departuresByStopId.getData().length);
                        for (Trip trip : trips) {
                            tripService.saveTrip(trip);
                        }
                    } else {
                        log.error("Error while fetching data for stop {}", stops.get(i).getName());
                    }
                } catch (Exception e) {
                    log.error("Error while fetching data for stop {}", stops.get(i).getName());
                    log.error(e.getMessage());
                }
            }
            log.info("---- Finished fetching departures ----");
        } else {
            log.error("No stops found. Please provide some stops in the database.");
        }
    }
}
