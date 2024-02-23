package de.whosfritz.railinsights.data.services.train_services;

import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.whosfritz.railinsights.data.repositories.LineRepository;
import de.whosfritz.railinsights.data.repositories.trip_repositories.TripsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainStatsService {

    private TripsRepository tripsRepository;
    private LineRepository lineRepository;

    public TrainStatsService(TripsRepository tripsRepository, LineRepository lineRepository) {
        this.tripsRepository = tripsRepository;
        this.lineRepository = lineRepository;
    }


    public Optional<List<Line>> getListOfTrainsByProduct(String product) {
        return lineRepository.findLinesByProduct(product);
    }

    public Optional<List<Line>> getListOfTrainsByProductName(String product) {
        return lineRepository.findLinesByProduct(product);
    }

    /**
     * Get a train by its lineID (e.g., ice-2234)
     *
     * @param lineID the lineID of the train
     * @return the train with the given lineID
     */
    public Line getTrainByLineID(String lineID) {

        return lineRepository.findByLineId(lineID).get();
    }

}
