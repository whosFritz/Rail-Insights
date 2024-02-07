package de.whosfritz.railinsights.data.services.station_services.sub;

import de.olech2412.adapter.dbadapter.exception.Result;
import de.olech2412.adapter.dbadapter.model.station.sub.ProductLine;
import de.whosfritz.railinsights.data.repositories.station_repositories.sub.ProductLineRepository;
import de.whosfritz.railinsights.jpa.JPAError;
import de.whosfritz.railinsights.jpa.JPAErrors;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class ProductLineService {

    @Autowired
    private ProductLineRepository productLineRepository;

    /**
     * Deletes all {@link ProductLine} objects.
     *
     * @param productLines the productLines to delete
     */
    public void deleteAll(List<ProductLine> productLines) {
        productLineRepository.deleteAll(productLines);
    }

    public Result<ProductLine, JPAError> isAlreadyInDatabase(ProductLine productLine) {
        try {
            Optional<ProductLine> productLinesFromDatabase = productLineRepository.findByProductLine(productLine.getProductLine());

            return productLinesFromDatabase.<Result<ProductLine, JPAError>>map(Result::success).orElseGet(() -> Result.error(new JPAError(JPAErrors.NOT_FOUND)));
        } catch (Exception e) {
            log.error("Error while checking if productLine is already in database: " + e.getMessage() + " " + e.getCause());
            log.error("ProductLine: " + productLine.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Deletes the {@link ProductLine} object with the given id.
     *
     * @param id the id of the address to delete
     */
    public Result<ProductLine, JPAError> deleteProductLineById(Long id) {
        try {
            ProductLine productLine = productLineRepository.findById(id).orElse(null);

            if (productLine == null)
                return Result.error(new JPAError(JPAErrors.NOT_FOUND));

            productLineRepository.deleteById(id);
            return Result.success(productLine);
        } catch (Exception e) {
            log.error("Error while deleting productLine: " + e.getMessage() + " " + e.getCause());
            log.error("ProductLine id: " + id);
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

    /**
     * Returns all {@link ProductLine} objects.
     *
     * @return all productLines
     */
    public Iterable<ProductLine> getAllProductLines() {
        return productLineRepository.findAll();
    }

    /**
     * Returns the {@link ProductLine} object with the given id.
     *
     * @param id the id of the productLine to return
     * @return the productLine with the given id
     */
    public ProductLine getProductLineById(Long id) {
        return productLineRepository.findById(id).orElse(null);
    }

    /**
     * Saves the given {@link ProductLine} object.
     *
     * @param productLine the productLine to save
     */
    @Transactional
    public Result<ProductLine, JPAError> saveProductLine(ProductLine productLine) {
        try {
            if (!isAlreadyInDatabase(productLine).isSuccess()) {
                return Result.success(productLineRepository.save(productLine));
            } else {
                return Result.error(new JPAError(JPAErrors.ALREADY_EXISTS));
            }
        } catch (Exception e) {
            log.error("Error while saving productLine: " + e.getMessage() + " " + e.getCause());
            log.error("ProductLine: " + productLine.toString());
            return Result.error(new JPAError(JPAErrors.UNKNOWN));
        }
    }

}
