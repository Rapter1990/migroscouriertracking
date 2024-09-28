package com.casestudy.migroscouriertracking.courier.exception;

/**
 * Exception thrown when a requested courier is not found.
 */
public class CourierNotFoundException extends RuntimeException {

    /**
     * Constructs a new CourierNotFoundException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public CourierNotFoundException(String message) {
        super(message);
    }

}
