package com.casestudy.migroscouriertracking.courier.utils;

import com.casestudy.migroscouriertracking.courier.model.Location;
import com.casestudy.migroscouriertracking.courier.model.enums.DistanceType;
import lombok.experimental.UtilityClass;

import java.time.Duration;
import java.time.LocalDateTime;


@UtilityClass
public class DistanceUtils {

    public static boolean isWithinRadius(double courierLat, double courierLng, double storeLat, double storeLng, double radiusInMeters) {
        double radiusInKm = radiusInMeters / 1000.0;
        Location courierLocation = new Location(courierLat, courierLng);
        Location storeLocation = new Location(storeLat, storeLng);
        double distance = DistanceCalculationUtil.calculateDistance(courierLocation, storeLocation, DistanceType.KILOMETERS);
        return distance <= radiusInKm;
    }

    public static double calculateDistance(double lat1, double lng1, double lat2, double lng2, DistanceType distanceType) {
        Location startLocation = new Location(lat1, lng1);
        Location endLocation = new Location(lat2, lng2);
        return DistanceCalculationUtil.calculateDistance(startLocation, endLocation, distanceType);
    }

    public static boolean isMoreThanOneMinuteAgo(LocalDateTime lastTimestamp, LocalDateTime currentTimestamp) {
        return Duration.between(lastTimestamp, currentTimestamp).toMinutes() > 1;
    }

}

