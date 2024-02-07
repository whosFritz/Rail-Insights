package de.whosfritz.railmetrics.data.services.trip_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.trip.sub.Remark;
import de.whosfritz.railmetrics.data.repositories.trip_repositories.sub.RemarkRepository;
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
public class RemarkService {

    @Autowired
    private RemarkRepository remarkRepository;

    /**
     * Deletes all {@link Remark} objects.
     *
     * @param remarks the remarks to delete
     */
    public void deleteAll(List<Remark> remarks) {
        remarkRepository.deleteAll(remarks);
    }

    public Result<Remark, JPAError> isAlreadyInDatabase(Remark remark) {
        try {
            Optional<Remark> remarkOptional = remarkRepository.findByText(remark.getText());

            return remarkOptional.<Result<Remark, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if remark is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Remark: " + remark.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Remark} object with the given id.
     *
     * @param id the id of the remark to delete
     */
    public Result<Remark, JPAError> deleteRemarkById(Long id) {
        try {
            Remark remark = remarkRepository.findById(id).orElse(null);

            if (remark == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            remarkRepository.deleteById(id);
            return Result.success(remark);
        } catch (Exception e) {
            log.error("Error while deleting remark: " + e.getMessage() + " " + e.getCause());
            log.error("Remark id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Remark} objects.
     *
     * @return all remarks
     */
    public Iterable<Remark> getAllRemarks() {
        return remarkRepository.findAll();
    }

    /**
     * Returns the {@link Remark} object with the given id.
     *
     * @param id the id of the remark to return
     * @return the remark with the given id
     */
    public Remark getRemarkById(Long id) {
        return remarkRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Remark} object.
     *
     * @param remark the remark to save
     */
    @Transactional
    public Result<Remark, JPAError> saveRemark(Remark remark) {
        try {
            if (!isAlreadyInDatabase(remark).isSuccess()) {
                remarkRepository.save(remark);
                return Result.success(remark);
            }

            return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
        } catch (Exception e) {
            log.error("Error while saving remark: " + e.getMessage() + " " + e.getCause());
            log.error("Remark: " + remark.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    public Result<Remark, JPAError> findByText(String text) {
        try {
            Optional<Remark> remarkOptional = remarkRepository.findByText(text);

            return remarkOptional.<Result<Remark, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if remark is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Remark: " + text);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
