package de.whosfritz.railinsights.data.services.train_services;

import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.whosfritz.railinsights.data.repositories.LineRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TrainStatsService {

    private LineRepository lineRepository;

    public TrainStatsService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Optional<List<Line>> getLinesByLineName(String lineName) {
        Optional<List<Line>> lines = lineRepository.findLinesByName(lineName);
        lines.ifPresent(l -> l.sort(Comparator.comparing(Line::getFahrtNr)));
        return lines;
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
