package com.casestudy.migroscouriertracking.courier.exception;

public class TimestampBeforeStoreCreateException extends RuntimeException {
    public TimestampBeforeStoreCreateException(String message) {
        super(message);
    }
}
