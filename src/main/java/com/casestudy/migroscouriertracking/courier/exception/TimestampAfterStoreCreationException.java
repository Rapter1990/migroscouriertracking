package com.casestudy.migroscouriertracking.courier.exception;

/**
 * Exception thrown when the timestamp is not after the nearest store's creation time.
 */
public class TimestampAfterStoreCreationException extends RuntimeException {

    /**
     * Constructs a new TimestampAfterStoreCreationException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public TimestampAfterStoreCreationException(String message) {
        super(message);
    }

}

