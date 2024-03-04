package de.whosfritz.railinsights.data.services;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.Operator;
import de.olech2412.adapter.dbadapter.model.stop.sub.Line;
import de.whosfritz.railinsights.data.repositories.LineRepository;
import de.whosfritz.railinsights.exception.JPAError;
import de.whosfritz.railinsights.exception.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class LineService {

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private LineRepository lineRepository;

    /**
     * Deletes all {@link Line} objects.
     *
     * @param lines the lines to delete
     */
    public void deleteAll(List<Line> lines) {
        lineRepository.deleteAll(lines);
    }

    public Result<Line, JPAError> isAlreadyInDatabase(Line line) {
        try {
            Optional<Line> lineOptional = lineRepository.findByLineIdAndFahrtNr(line.getLineId(), line.getFahrtNr());

            return lineOptional.<Result<Line, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if line is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Line: " + line.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    public Result<List<Line>, JPAError> getLinesNationalOrNationalExpress() {
        try {
            Optional<List<Line>> lines = lineRepository.findLinesByProductContains("national");
            lines.ifPresent(l -> l.sort(Comparator.comparing(Line::getName)));
            return lines.<Result<List<Line>, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while getting lines by product: " + e.getMessage() + " " + e.getCause());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Line} object with the given id.
     *
     * @param id the id of the line to delete
     */
    public Result<Line, JPAError> deleteLineById(Long id) {
        try {
            Line line = lineRepository.findById(id).orElse(null);

            if (line == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            lineRepository.deleteById(id);
            return Result.success(line);
        } catch (Exception e) {
            log.error("Error while deleting stop: " + e.getMessage() + " " + e.getCause());
            log.error("Line id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Line} objects.
     *
     * @return all lines
     */
    public List<Line> getAllLines() {
        return lineRepository.findAll();
    }

    /**
     * Returns the {@link Line} object with the given id.
     *
     * @param id the id of the line to return
     * @return the line with the given id
     */
    public Line getLineById(Long id) {
        return lineRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Line} object.
     *
     * @param line the lines to save
     */
    @Transactional
    public Result<Line, JPAError> saveLine(Line line) {
        try {
            if (!isAlreadyInDatabase(line).isSuccess()) {
                if (line.getOperator() != null) {
                    Result<Operator, JPAError> operatorFromDatabase = operatorService.isAlreadyInDatabase(line.getOperator());
                    if (operatorFromDatabase.isSuccess()) {
                        line.setOperator(operatorFromDatabase.getData());
                        lineRepository.save(line);
                        return Result.success(line);
                    }

                    Result<Operator, JPAError> operatorJPAErrorResult = operatorService.saveOperator(line.getOperator());

                    if (operatorJPAErrorResult.isSuccess()) {
                        line.setOperator(operatorJPAErrorResult.getData());
                        lineRepository.save(line);
                        return Result.success(line);
                    } else {
                        return Result.error(operatorJPAErrorResult.getError());
                    }
                } else {
                    log.error("Cant find operator for line: " + line.getLineId() + " " + line.getFahrtNr() + " save without");
                    lineRepository.save(line);
                    return Result.success(line);
                }
            }

            return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
        } catch (Exception e) {
            log.error("Error while saving line: " + e.getMessage() + " " + e.getCause());
            log.error("Line: " + line.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    public Result<Line, JPAError> findByLineIdAndFahrtNr(String lineId, String fahrtNr) {
        try {
            Optional<Line> lineOptional = lineRepository.findByLineIdAndFahrtNr(lineId, fahrtNr);

            return lineOptional.<Result<Line, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if line is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Line: " + lineId);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
