package com.casestudy.migroscouriertracking.courier.exception;

/**
 * Exception thrown when a timestamp is earlier than the store's creation time.
 */
public class TimestampBeforeStoreCreateException extends RuntimeException {

    /**
     * Constructs a new TimestampBeforeStoreCreateException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public TimestampBeforeStoreCreateException(String message) {
        super(message);
    }

}
