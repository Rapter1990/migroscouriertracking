package com.casestudy.migroscouriertracking.courier.utils;

import com.casestudy.migroscouriertracking.courier.model.enums.DistanceType;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link DistanceUtils} class, verifying the utility methods for distance
 * calculations, radius checks, and timestamp comparisons.
 */
class DistanceUtilsTest {

    @Test
    void utilityClass_ShouldNotBeInstantiated() {
        assertThrows(InvocationTargetException.class, () -> {
            // Attempt to use reflection to create an instance of the utility class
            java.lang.reflect.Constructor<DistanceUtils> constructor = DistanceUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }

    @Test
    public void testIsWithinRadiusReturnsTrueWhenWithinRadiusInKilometers() {

        double courierLat = 37.7749;
        double courierLng = -122.4194;
        double storeLat = 37.7750;
        double storeLng = -122.4183;
        double radiusInMeters = 100.0;
        double mockDistanceInKm = 0.0001; // Close enough to be within the radius

        // Directly call the static method without mocking
        boolean result = DistanceUtils.isWithinRadius(courierLat, courierLng, storeLat, storeLng, radiusInMeters);

        assertTrue(result, "Courier should be within the radius of the store.");

    }

    @Test
    public void testIsWithinRadiusReturnsFalseWhenOutsideRadiusInKilometers() {

        double courierLat = 37.7749;
        double courierLng = -122.4194;
        double storeLat = 37.7800;
        double storeLng = -122.4200;
        double radiusInMeters = 100.0;
        double mockDistanceInKm = 0.01; // Far enough to be outside the radius

        // Directly call the static method without mocking
        boolean result = DistanceUtils.isWithinRadius(courierLat, courierLng, storeLat, storeLng, radiusInMeters);

        assertFalse(result, "Courier should not be within the radius of the store.");

    }

    @Test
    public void testCalculateDistanceReturnsCorrectDistanceInKilometers() {

        double lat1 = 37.7749;
        double lng1 = -122.4194;
        double lat2 = 37.7750;
        double lng2 = -122.4183;

        double expectedDistanceInKm = haversine(lat1, lng1, lat2, lng2);
        double tolerance = 0.0001;

        // Directly call the static method without mocking
        double distance = DistanceUtils.calculateDistance(lat1, lng1, lat2, lng2, DistanceType.KILOMETERS);

        assertTrue(Math.abs(distance - expectedDistanceInKm) <= tolerance,
                "Calculated distance should be within the expected range.");

    }

    @Test
    public void testIsMoreThanOneMinuteAgoReturnsTrue() {

        LocalDateTime lastTimestamp = LocalDateTime.now().minusMinutes(2);
        LocalDateTime currentTimestamp = LocalDateTime.now();

        // Directly call the static method
        boolean result = DistanceUtils.isMoreThanOneMinuteAgo(lastTimestamp, currentTimestamp);

        assertTrue(result, "Timestamp should be more than one minute ago.");

    }

    @Test
    public void testIsMoreThanOneMinuteAgoReturnsFalse() {

        LocalDateTime lastTimestamp = LocalDateTime.now().minusSeconds(30);
        LocalDateTime currentTimestamp = LocalDateTime.now();

        // Directly call the static method
        boolean result = DistanceUtils.isMoreThanOneMinuteAgo(lastTimestamp, currentTimestamp);

        assertFalse(result, "Timestamp should not be more than one minute ago.");

    }

    /**
     * Haversine formula to calculate the distance between two points on the Earth's surface.
     */
    private double haversine(double lat1, double lng1, double lat2, double lng2) {

        final double R = 6371.0; // Radius of the Earth in kilometers
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;

    }

}
