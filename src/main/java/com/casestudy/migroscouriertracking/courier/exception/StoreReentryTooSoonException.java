package com.casestudy.migroscouriertracking.courier.exception;

/**
 * Exception thrown when a courier attempts to reenter the circumference of a store
 * within a restricted time frame.
 */
public class StoreReentryTooSoonException extends RuntimeException {

    /**
     * Constructs a new StoreReentryTooSoonException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public StoreReentryTooSoonException(String message) {
        super(message);
    }

}
