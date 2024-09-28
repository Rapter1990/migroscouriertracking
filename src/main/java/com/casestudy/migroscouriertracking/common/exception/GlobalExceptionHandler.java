package com.casestudy.migroscouriertracking.common.exception;

import com.casestudy.migroscouriertracking.common.model.CustomError;
import com.casestudy.migroscouriertracking.courier.exception.CourierNotFoundException;
import com.casestudy.migroscouriertracking.courier.exception.StoreFarAwayException;
import com.casestudy.migroscouriertracking.courier.exception.StoreNotFoundException;
import com.casestudy.migroscouriertracking.courier.exception.TimestampBeforeStoreCreateException;
import jakarta.validation.ConstraintViolationException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler class named {@link GlobalExceptionHandler} for the application, responsible for handling specific exceptions
 * thrown by the controllers and returning appropriate HTTP responses.
 * This class handles various types of exceptions including validation errors, runtime exceptions,
 * and custom exceptions related to the courier and store operations.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException thrown when validation on an argument annotated with
     * {@code @Valid} fails.
     *
     * @param ex the MethodArgumentNotValidException thrown
     * @return ResponseEntity containing the custom error response with validation details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(
                error -> {
                    String fieldName = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    subErrors.add(
                            CustomError.CustomSubError.builder()
                                    .field(fieldName)
                                    .message(message)
                                    .build()
                    );
                }
        );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Validation failed")
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles ConstraintViolationException thrown when a method parameter validation fails.
     *
     * @param constraintViolationException the ConstraintViolationException thrown
     * @return ResponseEntity containing the custom error response with constraint violation details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handlePathVariableErrors(final ConstraintViolationException constraintViolationException) {

        List<CustomError.CustomSubError> subErrors = new ArrayList<>();
        constraintViolationException.getConstraintViolations()
                .forEach(constraintViolation ->
                        subErrors.add(
                                CustomError.CustomSubError.builder()
                                        .message(constraintViolation.getMessage())
                                        .field(StringUtils.substringAfterLast(constraintViolation.getPropertyPath().toString(), "."))
                                        .value(constraintViolation.getInvalidValue() != null ? constraintViolation.getInvalidValue().toString() : null)
                                        .type(constraintViolation.getInvalidValue().getClass().getSimpleName())
                                        .build()
                        )
                );

        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.VALIDATION_ERROR.getName())
                .message("Constraint violation")
                .subErrors(subErrors)
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);

    }

    /**
     * Handles generic RuntimeException that may occur in the application.
     *
     * @param runtimeException the RuntimeException thrown
     * @return ResponseEntity containing the custom error response with the exception message
     */
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<?> handleRuntimeException(final RuntimeException runtimeException) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(runtimeException.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles CourierNotFoundException thrown when a courier is not found in the system.
     *
     * @param ex the CourierNotFoundException thrown
     * @return ResponseEntity containing the custom error response with the exception message
     */
    @ExceptionHandler(CourierNotFoundException.class)
    protected ResponseEntity<CustomError> handleCourierNotFound(final CourierNotFoundException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles StoreFarAwayException thrown when a store is considered too far away.
     *
     * @param ex the StoreFarAwayException thrown
     * @return ResponseEntity containing the custom error response with the exception message
     */
    @ExceptionHandler(StoreFarAwayException.class)
    protected ResponseEntity<CustomError> handleStoreFarAway(final StoreFarAwayException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles StoreNotFoundException thrown when a store is not found in the system.
     *
     * @param ex the StoreNotFoundException thrown
     * @return ResponseEntity containing the custom error response with the exception message
     */
    @ExceptionHandler(StoreNotFoundException.class)
    protected ResponseEntity<CustomError> handleStoreNotFound(final StoreNotFoundException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.NOT_FOUND)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles TimestampBeforeStoreCreateException thrown when an operation has a timestamp
     * that precedes the creation of the store.
     *
     * @param ex the TimestampBeforeStoreCreateException thrown
     * @return ResponseEntity containing the custom error response with the exception message
     */
    @ExceptionHandler(TimestampBeforeStoreCreateException.class)
    protected ResponseEntity<CustomError> handleTimestampBeforeStoreCreate(final TimestampBeforeStoreCreateException ex) {
        CustomError customError = CustomError.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .header(CustomError.Header.API_ERROR.getName())
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(customError, HttpStatus.BAD_REQUEST);
    }

}
