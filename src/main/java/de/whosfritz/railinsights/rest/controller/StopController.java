package de.whosfritz.railinsights.rest.controller;

import de.olech2412.adapter.dbadapter.model.stop.Stop;
import de.whosfritz.railinsights.data.repositories.stop_repositories.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class StopController {

    @Autowired
    private StopRepository stopRepository;

    // GET-Endpunkt zur Rückgabe eines Stops anhand seiner ID
    @GetMapping("/stops/{stopId}")
    public ResponseEntity<Stop> getStopById(@PathVariable("stopId") Long stopId) {
        Optional<Stop> stop = stopRepository.findByStopId(stopId);
        return stop.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/stops")
    public ResponseEntity<List<Stop>> findStopsByName(@RequestParam String name) {
        Optional<List<Stop>> stops = stopRepository.findByNameContainingIgnoreCase(name);
        return stops.map(stopList -> new ResponseEntity<>(stopList, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}

