package com.casestudy.migroscouriertracking.courier.exception;

/**
 * Exception thrown when a store is considered too far away for a specific operation.
 */
public class StoreFarAwayException extends RuntimeException {

    /**
     * Constructs a new StoreFarAwayException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public StoreFarAwayException(String message) {
        super(message);
    }

}
