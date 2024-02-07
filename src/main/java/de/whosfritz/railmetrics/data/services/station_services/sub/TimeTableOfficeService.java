package de.whosfritz.railmetrics.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.TimeTableOffice;
import de.whosfritz.railmetrics.data.repositories.station_repositories.sub.TimeTableOfficeRepository;
import de.whosfritz.railmetrics.data.services.trip_services.TripService;
import de.whosfritz.railmetrics.exception.jpa.JPAError;
import de.whosfritz.railmetrics.exception.jpa.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class TimeTableOfficeService {

    @Autowired
    private TimeTableOfficeRepository timeTableOfficeRepository;

    /**
     * Deletes all {@link TripService} objects.
     *
     * @param timeTableOffices the timeTableOffices to delete
     */
    public void deleteAll(List<TimeTableOffice> timeTableOffices) {
        timeTableOfficeRepository.deleteAll(timeTableOffices);
    }

    public Result<TimeTableOffice, JPAError> isAlreadyInDatabase(TimeTableOffice timeTableOffice) {
        try {
            Optional<TimeTableOffice> timeTableOfficesFromDatabase = timeTableOfficeRepository.findByEmailAndName(timeTableOffice.getEmail(), timeTableOffice.getName());

            return timeTableOfficesFromDatabase.<Result<TimeTableOffice, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if timeTableOffice is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("TimeTableOffice: " + timeTableOffice.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link TimeTableOffice} object with the given id.
     *
     * @param id the id of the timeTableOffices to delete
     */
    public Result<TimeTableOffice, JPAError> deleteTimeTableOfficeById(Long id) {
        try {
            TimeTableOffice timeTableOffice = timeTableOfficeRepository.findById(id).orElse(null);

            if (timeTableOffice == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            timeTableOfficeRepository.deleteById(id);
            return Result.success(timeTableOffice);
        } catch (Exception e) {
            log.error("Error while deleting timeTableOffice: " + e.getMessage() + " " + e.getCause());
            log.error("TimeTableOffice id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link TimeTableOffice} objects.
     *
     * @return all timeTableOffices
     */
    public Iterable<TimeTableOffice> getAllTimeTableOffices() {
        return timeTableOfficeRepository.findAll();
    }

    /**
     * Returns the {@link TimeTableOffice} object with the given id.
     *
     * @param id the id of the timeTableOffice to return
     * @return the timeTableOffice with the given id
     */
    public TimeTableOffice getTimeTableOfficeById(Long id) {
        return timeTableOfficeRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link TimeTableOffice} object.
     *
     * @param timeTableOffice the timeTableOffice to save
     */
    @Transactional
    public Result<TimeTableOffice, JPAError> saveTimeTableOffice(TimeTableOffice timeTableOffice) {
        try {
            if (!isAlreadyInDatabase(timeTableOffice).isSuccess()) {
                timeTableOfficeRepository.save(timeTableOffice);
                return Result.success(timeTableOffice);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving timeTableOffice: " + e.getMessage() + " " + e.getCause());
            log.error("TimeTableOffice: " + timeTableOffice.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
