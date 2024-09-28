package com.casestudy.migroscouriertracking.courier.strategy;

import com.casestudy.migroscouriertracking.courier.model.Location;

/**
 * Strategy for calculating distance in kilometers.
 */
public class DistanceInKilometersCalculatorStrategy implements DistanceCalculationStrategy {

    private final double earthRadiusInKilometers = 6371.0;

    /**
     * Calculates the distance between two locations in kilometers.
     *
     * @param startLoc the starting location
     * @param endLoc   the ending location
     * @return the distance between the two locations in kilometers
     */
    @Override
    public double calculateDistance(Location startLoc, Location endLoc) {
        double centralAngle = calculateGreatCircleDistance(startLoc, endLoc);
        return earthRadiusInKilometers * centralAngle;
    }

    /**
     * Calculates the central angle between two locations using the
     * Haversine formula.
     *
     * @param startLoc the starting location
     * @param endLoc   the ending location
     * @return the central angle in radians
     */
    private double calculateGreatCircleDistance(Location startLoc, Location endLoc) {
        double latOne = Math.toRadians(startLoc.getLatitude());
        double lngOne = Math.toRadians(startLoc.getLongitude());
        double latTwo = Math.toRadians(endLoc.getLatitude());
        double lngTwo = Math.toRadians(endLoc.getLongitude());

        double diffOfLat = latTwo - latOne;
        double diffOfLng = lngTwo - lngOne;

        double ax = Math.sin(diffOfLat / 2) * Math.sin(diffOfLat / 2) +
                Math.cos(latOne) * Math.cos(latTwo) *
                        Math.sin(diffOfLng / 2) * Math.sin(diffOfLng / 2);
        return 2 * Math.atan2(Math.sqrt(ax), Math.sqrt(1 - ax));
    }

}
