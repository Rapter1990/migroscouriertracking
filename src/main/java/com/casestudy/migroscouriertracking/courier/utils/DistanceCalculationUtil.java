package com.casestudy.migroscouriertracking.courier.utils;

import com.casestudy.migroscouriertracking.courier.model.Location;
import com.casestudy.migroscouriertracking.courier.model.enums.DistanceType;
import com.casestudy.migroscouriertracking.courier.strategy.DistanceCalculationStrategy;
import com.casestudy.migroscouriertracking.courier.strategy.DistanceInKilometersCalculatorStrategy;
import com.casestudy.migroscouriertracking.courier.strategy.DistanceInMetersCalculatorStrategy;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;


@UtilityClass
public class DistanceCalculationUtil {

    private final Map<DistanceType, DistanceCalculationStrategy> distanceCalculationStrategies = new HashMap<>() {{
        put(DistanceType.METERS, new DistanceInMetersCalculatorStrategy());
        put(DistanceType.KILOMETERS, new DistanceInKilometersCalculatorStrategy());
    }};


    public static double calculateDistance(Location startLoc, Location endLoc, DistanceType distanceType) {
        DistanceCalculationStrategy strategy = distanceCalculationStrategies.get(distanceType);
        if (strategy != null) {
            return strategy.calculateDistance(startLoc, endLoc);
        } else {
            throw new IllegalArgumentException("Invalid distance type");
        }
    }
}

