package com.casestudy.migroscouriertracking.courier.strategy;

import com.casestudy.migroscouriertracking.courier.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DistanceInMetersCalculatorStrategyTest {

    private DistanceInMetersCalculatorStrategy distanceCalculator;

    @BeforeEach
    public void setUp() {
        distanceCalculator = new DistanceInMetersCalculatorStrategy();
    }

    @Test
    public void testCalculateDistance_sameLocation() {
        // Given
        Location startLoc = new Location(39.9255, 32.8662); // Ankara
        Location endLoc = new Location(39.9255, 32.8662); // Ankara

        // When
        double distance = distanceCalculator.calculateDistance(startLoc, endLoc);

        // Then
        assertEquals(0.0, distance, 0.001); // Distance should be 0 meters
    }

}