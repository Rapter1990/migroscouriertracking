package com.casestudy.migroscouriertracking.courier.utils;

import com.casestudy.migroscouriertracking.courier.model.Location;
import com.casestudy.migroscouriertracking.courier.model.enums.DistanceType;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Utility class named {@link DistanceUtils} for various distance-related operations.
 */
@UtilityClass
public class DistanceUtils {

    /**
     * Checks if a courier's location is within a specified radius of a store's location.
     *
     * @param courierLat   the latitude of the courier's location
     * @param courierLng   the longitude of the courier's location
     * @param storeLat     the latitude of the store's location
     * @param storeLng     the longitude of the store's location
     * @param radiusInMeters the radius in meters within which to check
     * @return true if the courier is within the specified radius of the store; false otherwise
     */
    public boolean isWithinRadius(double courierLat, double courierLng, double storeLat, double storeLng, double radiusInMeters) {
        double radiusInKm = radiusInMeters / 1000.0;
        Location courierLocation = new Location(courierLat, courierLng);
        Location storeLocation = new Location(storeLat, storeLng);
        double distance = DistanceCalculationUtil.calculateDistance(courierLocation, storeLocation, DistanceType.KILOMETERS);
        return distance <= radiusInKm;
    }

    /**
     * Calculates the distance between two geographic coordinates.
     *
     * @param lat1        the latitude of the first location
     * @param lng1        the longitude of the first location
     * @param lat2        the latitude of the second location
     * @param lng2        the longitude of the second location
     * @param distanceType the type of distance to calculate (e.g., METERS or KILOMETERS)
     * @return the calculated distance between the two locations
     */
    public double calculateDistance(double lat1, double lng1, double lat2, double lng2, DistanceType distanceType) {
        Location startLocation = new Location(lat1, lng1);
        Location endLocation = new Location(lat2, lng2);
        return DistanceCalculationUtil.calculateDistance(startLocation, endLocation, distanceType);
    }

    /**
     * Checks if the specified time is more than one minute ago compared to the current timestamp.
     *
     * @param lastTimestamp   the last timestamp to check
     * @param currentTimestamp the current timestamp to compare against
     * @return true if the last timestamp is more than one minute ago; false otherwise
     */
    public boolean isMoreThanOneMinuteAgo(LocalDateTime lastTimestamp, LocalDateTime currentTimestamp) {
        return Duration.between(lastTimestamp, currentTimestamp).toMinutes() > 1;
    }

}

