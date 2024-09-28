package com.casestudy.migroscouriertracking.courier.exception;

/**
 * Exception thrown when a requested store is not found.
 */
public class StoreNotFoundException extends RuntimeException {

    /**
     * Constructs a new StoreNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public StoreNotFoundException(String message) {
        super(message);
    }

}
