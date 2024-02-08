package de.whosfritz.railinsights.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.olech2412.adapter.dbadapter.model.station.Station;
import de.whosfritz.railinsights.data.repositories.station_repositories.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class StationController {

    @Autowired
    private StationRepository stationRepository;

    // GET-Endpunkt zum Finden einer Station anhand ihrer stationId
    @GetMapping("/stations/{stationId}")
    public ResponseEntity<Station> getStationById(@PathVariable("stationId") Long stationId) throws JsonProcessingException {
        Optional<Station> station = stationRepository.findByStationId(stationId);

        return station.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // GET-Endpunkt zum Finden von Stationen nach Namen
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> findStationsByName(@RequestParam String name) {
        Optional<List<Station>> stations = stationRepository.findByNameContainingIgnoreCase(name);
        return stations.map(stationList -> new ResponseEntity<>(stationList, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
