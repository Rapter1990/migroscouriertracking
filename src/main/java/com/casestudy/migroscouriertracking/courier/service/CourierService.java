package com.casestudy.migroscouriertracking.courier.service;

import com.casestudy.migroscouriertracking.courier.exception.CourierNotFoundException;
import com.casestudy.migroscouriertracking.courier.exception.StoreFarAwayException;
import com.casestudy.migroscouriertracking.courier.exception.StoreNotFoundException;
import com.casestudy.migroscouriertracking.courier.exception.TimestampBeforeStoreCreateException;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CourierService {

    private final CourierRepository courierRepository;
    private final StoreRepository storeRepository;

    private final CourierEntityToCourierMapper courierEntityToCourierMapper = CourierEntityToCourierMapper.initialize();

    public void logCourierLocation(LogCourierLocationRequest logRequest) {
        String courierId = logRequest.getCourierId();
        double lat = logRequest.getLat();
        double lng = logRequest.getLng();
        LocalDateTime timestamp = logRequest.getTimestamp();
        List<StoreEntity> stores = storeRepository.findAll();

        Optional.ofNullable(stores)
                .orElseThrow(() -> new StoreNotFoundException("No stores found in the database."));

        boolean travelEntrySaved = stores.stream().anyMatch(store -> {
            if (DistanceUtils.isWithinRadius(lat, lng, store.getLat(), store.getLng(), 100.0)) {
                if (timestamp.isBefore(store.getCreatedAt())) {
                    throw new TimestampBeforeStoreCreateException("Timestamp is before store's creation time.");
                }

                CourierEntity lastTravel = findLastTravelEntry(courierId, store.getName(), timestamp);
                if (lastTravel == null || DistanceUtils.isMoreThanOneMinuteAgo(lastTravel.getTimestamp(), timestamp)) {
                    CourierEntity courier = new CourierEntity(courierId, lat, lng, store.getName(), timestamp);
                    courierRepository.save(courier);
                    return true;
                }
            }
            return false;
        });

        if (!travelEntrySaved) {
            throw new StoreFarAwayException("Courier is far away from all stores.");
        }
    }

    private CourierEntity findLastTravelEntry(String courierId, String storeName, LocalDateTime currentTimestamp) {
        LocalDateTime oneMinuteAgo = currentTimestamp.minusMinutes(1);
        return courierRepository.findByCourierIdAndStoreNameAndTimestampBetween(courierId, storeName, oneMinuteAgo, currentTimestamp)
                .stream()
                .max(Comparator.comparing(CourierEntity::getTimestamp))
                .orElse(null);
    }

    public List<Courier> getPastTravelsByCourierId(String courierId) {
        List<CourierEntity> entities = courierRepository.findByCourierId(courierId);
        Optional.ofNullable(entities)
                .filter(e -> !e.isEmpty())
                .orElseThrow(() -> new CourierNotFoundException("Courier with ID " + courierId + " not found."));
        return courierEntityToCourierMapper.map(entities);
    }

    public List<Courier> getTravelsByCourierIdStoreNameAndTimeRange(TravelQueryRequest request) {
        String courierId = request.getCourierId();
        String storeName = request.getStoreName();
        LocalDateTime start = request.getStart();
        LocalDateTime end = request.getEnd();

        List<CourierEntity> entities = courierRepository.findByCourierIdAndStoreNameAndTimestampBetween(courierId, storeName, start, end);
        Optional.ofNullable(entities)
                .filter(e -> !e.isEmpty())
                .orElseThrow(() -> new CourierNotFoundException("No travels found for Courier ID " + courierId + " in store " + storeName + " between " + start + " and " + end + "."));
        return courierEntityToCourierMapper.map(entities);
    }

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
