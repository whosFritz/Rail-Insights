package de.whosfritz.railmetrics.data.services;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.Operator;
import de.whosfritz.railmetrics.data.repositories.OperatorRepository;
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
public class OperatorService {

    @Autowired
    private OperatorRepository operatorRepository;

    /**
     * Deletes all {@link Operator} objects.
     *
     * @param operators the operators to delete
     */
    public void deleteAll(List<Operator> operators) {
        operatorRepository.deleteAll(operators);
    }

    public Result<Operator, JPAError> isAlreadyInDatabase(Operator operator) {
        try {
            Optional<Operator> operatorsFromDatabase = operatorRepository.findByOperatorId(operator.getOperatorId());

            return operatorsFromDatabase.<Result<Operator, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if operator is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Operator: " + operator.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link Operator} object with the given id.
     *
     * @param id the id of the operator to delete
     */
    public Result<Operator, JPAError> deleteOperatorById(Long id) {
        try {
            Operator operator = operatorRepository.findById(id).orElse(null);

            if (operator == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            operatorRepository.deleteById(id);
            return Result.success(operator);
        } catch (Exception e) {
            log.error("Error while deleting operator: " + e.getMessage() + " " + e.getCause());
            log.error("Operator id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link Operator} objects.
     *
     * @return all operators
     */
    public Iterable<Operator> getAllOperators() {
        return operatorRepository.findAll();
    }

    /**
     * Returns the {@link Operator} object with the given id.
     *
     * @param id the id of the operator to return
     * @return the operator with the given id
     */
    public Operator getOpertatorById(Long id) {
        return operatorRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link Operator} object.
     *
     * @param operator the operator to save
     */
    @Transactional
    public Result<Operator, JPAError> saveOperator(Operator operator) {
        try {
            if (!isAlreadyInDatabase(operator).isSuccess()) {
                operatorRepository.save(operator);
                return Result.success(operator);
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving operator: " + e.getMessage() + " " + e.getCause());
            log.error("Operator: " + operator.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    public Result<Operator, JPAError> findOperatorByOperatorId(String operatorId) {
        try {
            Optional<Operator> operatorOptional = operatorRepository.findByOperatorId(operatorId);

            return operatorOptional.<Result<Operator, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if operator is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("Operator: " + operatorId);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
