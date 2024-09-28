package com.casestudy.migroscouriertracking.courier.utils;

import com.casestudy.migroscouriertracking.courier.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for the {@link DistanceCalculationUtil} class, validating distance calculations
 * between {@link Location} objects and handling exceptions for invalid distance types.
 */
class DistanceCalculationUtilTest {

    private Location startLoc;
    private Location endLoc;

    @BeforeEach
    void setUp() {
        // Initialize some sample locations for testing
        startLoc = new Location(39.9255, 32.8662); // Ankara
        endLoc = new Location(39.9255, 32.8662); // Ankara
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidDistanceType() {

        // Given & When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            DistanceCalculationUtil.calculateDistance(startLoc, endLoc, null);
        });

        // Then
        assertEquals("Invalid distance type", exception.getMessage());

    }
}
