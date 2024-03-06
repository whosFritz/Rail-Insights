package de.whosfritz.railinsights.rest.controller;

import de.olech2412.adapter.dbadapter.model.trip.Trip;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TripController {

    @Autowired
    private TripsRepository tripRepository;

    // GET-Endpunkt zur Rückgabe eines Trips anhand seiner tripId und Stop
    @GetMapping("/trips")
    public ResponseEntity<List<Trip>> findTripsByTripIdAndStop(@RequestParam String tripId) {
        Optional<List<Trip>> trips = tripRepository.findAllByTripId(tripId);
        return trips.map(tripList -> new ResponseEntity<>(tripList, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET-Endpunkt zur Rückgabe aller Trips, die nach einem bestimmten Zeitpunkt erstellt wurden
    @GetMapping("/trips/stopId/when")
    public ResponseEntity<List<Trip>> findTripsWhenAfter(@RequestParam Long stopId,
                                                         @RequestParam LocalDateTime when) {
        Optional<List<Trip>> trips = tripRepository.findAllByStopIdAndWhenIsAfter(stopId, when);
        return trips.map(tripList -> new ResponseEntity<>(tripList, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET-Endpunkt zur Rückgabe aller Trips, die nach einem bestimmten Zeitpunkt erstellt wurden
    // und die eine bestimmte Line productName haben, mit Pagination-Unterstützung
    @GetMapping("/trips/line-productName")
    public ResponseEntity<Page<List<Trip>>> findTripsByLineProductName(@RequestParam String lineName,
                                                                       @RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "100") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<List<Trip>> tripsPage = tripRepository.findAllByLineName(lineName, pageable);
        if (!tripsPage.isEmpty()) {
            return new ResponseEntity<>(tripsPage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
