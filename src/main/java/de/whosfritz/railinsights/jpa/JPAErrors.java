package de.whosfritz.railinsights.jpa;

import lombok.Getter;

/**
 * This enum represents an error from the JPA (not found, already exists, etc.)
 * Error codes: 901 - 999
 */
@Getter
public enum JPAErrors {

    NOT_FOUND("The entity was not found", 901),

    ALREADY_EXISTS("The entity already exists", 902),

    CONSTRAINT_VIOLATION("A constraint was violated", 903),

    UNKNOWN("An unknown error occurred", 999);

    private final String message;

    private final int code;

    JPAErrors(String message, int code) {
        this.message = message;
        this.code = code;
    }

}
