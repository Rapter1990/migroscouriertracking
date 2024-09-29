package com.casestudy.migroscouriertracking.courier.service;

import com.casestudy.migroscouriertracking.courier.exception.*;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.dto.request.LogCourierLocationRequest;
import com.casestudy.migroscouriertracking.courier.model.dto.request.TravelQueryRequest;
import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import com.casestudy.migroscouriertracking.courier.model.entity.StoreEntity;
import com.casestudy.migroscouriertracking.courier.model.enums.DistanceType;
import com.casestudy.migroscouriertracking.courier.model.mapper.CourierEntityToCourierMapper;
import com.casestudy.migroscouriertracking.courier.repository.CourierRepository;
import com.casestudy.migroscouriertracking.courier.repository.StoreRepository;
import com.casestudy.migroscouriertracking.courier.utils.DistanceUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Service class named {@link CourierService} responsible for handling courier-related operations,
 * including logging courier locations and retrieving travel records.
 */
@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final StoreRepository storeRepository;

    private final CourierEntityToCourierMapper courierEntityToCourierMapper = CourierEntityToCourierMapper.initialize();

    /**
     * Logs the location of a courier based on the provided log request.
     * Validates if the courier is within a certain radius of any store,
     * ensures the timestamp is not before the store's creation time,
     * and saves the courier's location if the last entry was more than one minute ago.
     *
     * @param logRequest the request object containing courier location details
     * @throws StoreNotFoundException if no stores are found in the database
     * @throws TimestampBeforeStoreCreateException if the timestamp is before the store's creation time
     * @throws StoreFarAwayException if the courier is far away from all stores
     */
    public void logCourierLocation(LogCourierLocationRequest logRequest) {
        String courierId = logRequest.getCourierId();
        double lat = logRequest.getLat();
        double lng = logRequest.getLng();
        LocalDateTime timestamp = logRequest.getTimestamp();
        List<StoreEntity> stores = storeRepository.findAll();

        Optional.ofNullable(stores)
                .orElseThrow(() -> new StoreNotFoundException("No stores found in the database."));

        stores.stream()
                .filter(store -> DistanceUtils.isWithinRadius(lat, lng, store.getLat(), store.getLng(), 100.0))
                .findFirst()
                .ifPresentOrElse(store -> {
                    if (timestamp.isBefore(store.getCreatedAt())) {
                        throw new TimestampBeforeStoreCreateException("Timestamp is before store's creation time.");
                    }

                    // Find the last travel entry for the courier at this store
                    CourierEntity lastTravel = findLastTravelEntry(courierId, store.getName(), timestamp);
                    if (lastTravel == null || DistanceUtils.isMoreThanOneMinuteAgo(lastTravel.getTimestamp(), timestamp)) {
                        CourierEntity courier = CourierEntity.builder()
                                .courierId(courierId)
                                .lat(lat)
                                .lng(lng)
                                .storeName(store.getName())
                                .timestamp(timestamp)
                                .build();
                        courierRepository.save(courier); // Exit after saving the first valid entry
                    } else {
                        throw new StoreReentryTooSoonException("Reentry to the same store's circumference is too soon. Please wait before logging again.");
                    }
                }, () -> {
                    throw new StoreFarAwayException("Courier is far away from all stores.");
                });

    }

    /**
     * Finds the last travel entry for a specific courier within a given timestamp range.
     *
     * @param courierId        the unique identifier of the courier
     * @param storeName        the name of the store
     * @param currentTimestamp the current timestamp to compare with
     * @return the last travel entry for the courier, or null if not found
     */
    private CourierEntity findLastTravelEntry(String courierId, String storeName, LocalDateTime currentTimestamp) {
        LocalDateTime oneMinuteAgo = currentTimestamp.minusMinutes(1);
        return courierRepository.findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(courierId, storeName, oneMinuteAgo, currentTimestamp)
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves the past travels of a courier by their unique ID.
     *
     * @param courierId the unique identifier of the courier
     * @return a list of Courier objects representing the courier's past travels
     * @throws CourierNotFoundException if no travels are found for the given courier ID
     */
    public List<Courier> getPastTravelsByCourierId(String courierId) {
        List<CourierEntity> entities = courierRepository.findByCourierId(courierId);
        Optional.ofNullable(entities)
                .filter(e -> !e.isEmpty())
                .orElseThrow(() -> new CourierNotFoundException("Courier with ID " + courierId + " not found."));
        return courierEntityToCourierMapper.map(entities);
    }

    /**
     * Retrieves travels of a courier within a specified store name and time range.
     *
     * @param request the request object containing courier ID, store name, start time, and end time
     * @return a list of Courier objects representing the courier's travels
     * @throws CourierNotFoundException if no travels are found for the given criteria
     */
    public List<Courier> getTravelsByCourierIdStoreNameAndTimeRange(TravelQueryRequest request) {
        String courierId = request.getCourierId();
        String storeName = request.getStoreName();
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        List<CourierEntity> entities = courierRepository.findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(courierId, storeName, start, end);
        Optional.ofNullable(entities)
                .filter(e -> !e.isEmpty())
                .orElseThrow(() -> new CourierNotFoundException("No travels found for Courier ID " + courierId + " in store " + storeName + " between " + start + " and " + end + "."));
        return courierEntityToCourierMapper.map(entities);
    }

    /**
     * Calculates the total travel distance of a courier based on their travel records.
     *
     * @param courierId the unique identifier of the courier
     * @return the total travel distance in kilometers
     * @throws CourierNotFoundException if no travel records are found for the given courier ID
     */
    public double getTotalTravelDistance(String courierId) {
        List<CourierEntity> travels = courierRepository.findByCourierIdOrderByTimestampAsc(courierId);
        Optional.ofNullable(travels)
                .filter(t -> !t.isEmpty())
                .orElseThrow(() -> new CourierNotFoundException("No travel records found for Courier ID " + courierId + "."));

        return IntStream.range(1, travels.size())
                .mapToDouble(i -> {
                    CourierEntity prevTravel = travels.get(i - 1);
                    CourierEntity currentTravel = travels.get(i);
                    return DistanceUtils.calculateDistance(prevTravel.getLat(), prevTravel.getLng(),
                            currentTravel.getLat(), currentTravel.getLng(), DistanceType.KILOMETERS);
                })
                .sum();

    }

}
