package com.casestudy.migroscouriertracking.courier.strategy;


import com.casestudy.migroscouriertracking.courier.model.Location;

/**
 * Interface named {@link DistanceCalculationStrategy} representing a strategy for calculating the distance
 * between two geographical locations.
 */
public interface DistanceCalculationStrategy {

    /**
     * Calculates the distance between two specified locations.
     *
     * @param startLoc the starting location
     * @param endLoc   the ending location
     * @return the calculated distance between the two locations in the desired unit
     */
    double calculateDistance(Location startLoc, Location endLoc);

}
