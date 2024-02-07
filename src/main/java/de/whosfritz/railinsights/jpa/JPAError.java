package de.whosfritz.railinsights.jpa;

import de.olech2412.adapter.dbadapter.exception.Error;

/**
 * This class can be used to represent an error from the JPA (save, delete, etc.)
 */
public class JPAError implements Error {

    private final JPAErrors jpaError;

    public JPAError(JPAErrors jpaError) {
        this.jpaError = jpaError;
    }

    @Override
    public String getMessage() {
        return this.jpaError.getMessage();
    }

    @Override
    public int getCode() {
        return this.jpaError.getCode();
    }

    @Override
    public String getError() {
        return String.format("%s: %s", getCode(), getMessage());
    }
}