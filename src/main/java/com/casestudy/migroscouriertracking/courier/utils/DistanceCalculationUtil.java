package com.casestudy.migroscouriertracking.courier.utils;

import com.casestudy.migroscouriertracking.courier.model.Location;
import com.casestudy.migroscouriertracking.courier.model.enums.DistanceType;
import com.casestudy.migroscouriertracking.courier.strategy.DistanceCalculationStrategy;
import com.casestudy.migroscouriertracking.courier.strategy.DistanceInKilometersCalculatorStrategy;
import com.casestudy.migroscouriertracking.courier.strategy.DistanceInMetersCalculatorStrategy;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class named {@link DistanceCalculationUtil} for calculating distances between two locations using various strategies.
 */
@UtilityClass
public class DistanceCalculationUtil {

    private final Map<DistanceType, DistanceCalculationStrategy> distanceCalculationStrategies = new HashMap<>() {{
        put(DistanceType.METERS, new DistanceInMetersCalculatorStrategy());
        put(DistanceType.KILOMETERS, new DistanceInKilometersCalculatorStrategy());
    }};

    /**
     * Calculates the distance between two locations based on the specified distance type.
     *
     * @param startLoc   the starting location
     * @param endLoc     the ending location
     * @param distanceType the type of distance to calculate (e.g., METERS or KILOMETERS)
     * @return the calculated distance between the two locations
     * @throws IllegalArgumentException if the specified distance type is invalid
     */
    public double calculateDistance(Location startLoc, Location endLoc, DistanceType distanceType) {
        DistanceCalculationStrategy strategy = distanceCalculationStrategies.get(distanceType);
        if (strategy != null) {
            return strategy.calculateDistance(startLoc, endLoc);
        } else {
            throw new IllegalArgumentException("Invalid distance type");
        }
    }

}
