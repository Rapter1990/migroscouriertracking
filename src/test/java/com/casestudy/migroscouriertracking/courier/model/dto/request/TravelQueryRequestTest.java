package com.casestudy.migroscouriertracking.courier.model.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for the {@link TravelQueryRequest} class, validating constraints on
 * start and end times, UUID format, and store name properties.
 */
class TravelQueryRequestTest {

    private final Validator validator;

    public TravelQueryRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenStartTimeIsAfterEndTime() {

        // Given
        LocalDateTime start = LocalDateTime.now().plusDays(1); // Future date
        LocalDateTime end = LocalDateTime.now(); // Current date

        TravelQueryRequest request = TravelQueryRequest.builder()
                .courierId("123e4567-e89b-12d3-a456-426614174000")
                .storeName("Store")
                .start(start)
                .end(end)
                .build();

        // When
        Set<ConstraintViolation<TravelQueryRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Violations should be present");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Start time must be before end time")),
                "Violation for invalid time range should be present");

    }

    @Test
    void shouldPassWhenValidTimeRangeProvided() {

        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1); // Past date
        LocalDateTime end = LocalDateTime.now().plusDays(1); // Future date

        TravelQueryRequest request = TravelQueryRequest.builder()
                .courierId("123e4567-e89b-12d3-a456-426614174000")
                .storeName("Store")
                .start(start)
                .end(end)
                .build();

        // When
        Set<ConstraintViolation<TravelQueryRequest>> violations = validator.validate(request);

        // Then
        assertTrue(violations.isEmpty(), "No violations should be present for a valid time range");

    }

    @Test
    void shouldFailOnInvalidUUIDFormat() {

        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        TravelQueryRequest request = TravelQueryRequest.builder()
                .courierId("invalid-uuid")
                .storeName("Store")
                .start(start)
                .end(end)
                .build();

        // When
        Set<ConstraintViolation<TravelQueryRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Violations should be present for invalid UUID format");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Invalid UUID format")),
                "Violation for invalid UUID should be present");

    }

    @Test
    void shouldFailWhenStoreNameIsBlank() {

        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        TravelQueryRequest request = TravelQueryRequest.builder()
                .courierId("123e4567-e89b-12d3-a456-426614174000")
                .storeName("")
                .start(start)
                .end(end)
                .build();

        // When
        Set<ConstraintViolation<TravelQueryRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Violations should be present for blank store name");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Store name cannot be blank")),
                "Violation for blank store name should be present");

    }

    @Test
    void shouldFailWhenStartIsNull() {

        // Given
        LocalDateTime end = LocalDateTime.now().plusDays(1);

        TravelQueryRequest request = TravelQueryRequest.builder()
                .courierId("123e4567-e89b-12d3-a456-426614174000")
                .storeName("Store")
                .start(null)
                .end(end)
                .build();

        // When
        Set<ConstraintViolation<TravelQueryRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Violations should be present for null start time");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("Start time cannot be null")),
                "Violation for null start time should be present");
    }

    @Test
    void shouldFailWhenEndIsNull() {

        // Given
        LocalDateTime start = LocalDateTime.now().minusDays(1);

        TravelQueryRequest request = TravelQueryRequest.builder()
                .courierId("123e4567-e89b-12d3-a456-426614174000")
                .storeName("Store")
                .start(start)
                .end(null)
                .build();

        // When
        Set<ConstraintViolation<TravelQueryRequest>> violations = validator.validate(request);

        // Then
        assertFalse(violations.isEmpty(), "Violations should be present for null end time");
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().equals("End time cannot be null")),
                "Violation for null end time should be present");

    }

}