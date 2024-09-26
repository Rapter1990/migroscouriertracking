package com.casestudy.migroscouriertracking.courier.strategy;


import com.casestudy.migroscouriertracking.courier.model.Location;

public interface DistanceCalculationStrategy {

    double calculateDistance(Location startLoc, Location endLoc);

}
