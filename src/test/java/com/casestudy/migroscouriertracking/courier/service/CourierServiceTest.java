package com.casestudy.migroscouriertracking.courier.service;

import com.casestudy.migroscouriertracking.base.AbstractBaseServiceTest;
import com.casestudy.migroscouriertracking.courier.exception.StoreFarAwayException;
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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the {@link CourierService} class, validating methods related to
 * logging courier locations, retrieving past travels, and calculating total travel distance.
 */
class CourierServiceTest extends AbstractBaseServiceTest {

    @InjectMocks
    private CourierService courierService;

    @Mock
    private CourierRepository courierRepository;

    @Mock
    private StoreRepository storeRepository;

    private final CourierEntityToCourierMapper courierEntityToCourierMapper = CourierEntityToCourierMapper.initialize();


    @Test
    void logCourierLocation_shouldSaveCourierLocation_ifWithinRadiusAndTimestampValid() {

        // Given
        String courierId = "courier1";
        double lat = 37.7749;
        double lng = -122.4194;
        LocalDateTime timestamp = LocalDateTime.now();

        LogCourierLocationRequest logRequest = new LogCourierLocationRequest(courierId, lat, lng, timestamp);
        StoreEntity store = new StoreEntity("store1", 37.7750, -122.4183, LocalDateTime.now().minusMinutes(10));

        CourierEntity courierEntity = new CourierEntity(UUID.randomUUID().toString(), courierId, lat, lng, store.getName(), timestamp);

        // When
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(courierRepository.save(any(CourierEntity.class))).thenReturn(courierEntity);

        // Then
        courierService.logCourierLocation(logRequest);

        // Verify
        verify(courierRepository).save(any());

    }

    @Test
    void logCourierLocation_shouldThrowTimestampBeforeStoreCreateException_ifTimestampIsBeforeStoreCreation() {

        // Given
        LogCourierLocationRequest logRequest = new LogCourierLocationRequest("courier1", 37.7749, -122.4194, LocalDateTime.now());
        StoreEntity store = new StoreEntity("store1", 37.7750, -122.4183, LocalDateTime.now().plusMinutes(10));

        // When
        when(storeRepository.findAll()).thenReturn(List.of(store));

        // Then
        assertThrows(TimestampBeforeStoreCreateException.class, () -> courierService.logCourierLocation(logRequest));

        // Verify
        verify(storeRepository).findAll();

    }

    @Test
    void logCourierLocation_shouldThrowStoreFarAwayException_ifCourierIsFarAwayFromAllStores() {

        // Given
        LogCourierLocationRequest logRequest = new LogCourierLocationRequest("courier1", 37.7749, -122.4194, LocalDateTime.now());
        StoreEntity store = new StoreEntity("store1", 37.7800, -122.4200, LocalDateTime.now().minusMinutes(10));

        // When
        when(storeRepository.findAll()).thenReturn(List.of(store));

        // Then
        assertThrows(StoreFarAwayException.class, () -> courierService.logCourierLocation(logRequest));

        // Verify
        verify(storeRepository).findAll();

    }

    @Test
    void getPastTravelsByCourierId_shouldReturnTravelsForGivenCourierId() {

        // Given
        String courierId = "courier1";
        List<CourierEntity> courierEntities = List.of(new CourierEntity(UUID.randomUUID().toString(), courierId, 37.7749, -122.4194, "store1", LocalDateTime.now()));
        List<Courier> couriers = courierEntityToCourierMapper.map(courierEntities);

        // When
        when(courierRepository.findByCourierId(courierId)).thenReturn(courierEntities);

        // Then
        List<Courier> result = courierService.getPastTravelsByCourierId(courierId);

        assertFalse(result.isEmpty());
        assertEquals(couriers.get(0).getCourierId(), result.get(0).getCourierId());

        // Verify
        verify(courierRepository).findByCourierId(courierId);

    }

    @Test
    void getTravelsByCourierIdStoreNameAndTimeRange_shouldReturnTravelsWithinTimeRange() {

        // Given
        TravelQueryRequest request = new TravelQueryRequest("courier1", "store1", LocalDateTime.now().minusHours(1), LocalDateTime.now());
        List<CourierEntity> courierEntities = List.of(new CourierEntity(UUID.randomUUID().toString(), "courier1", 37.7749, -122.4194, "store1", LocalDateTime.now()));
        List<Courier> couriers = courierEntityToCourierMapper.map(courierEntities);

        // When
        when(courierRepository.findByCourierIdAndStoreNameAndTimestampBetween(request.getCourierId(), request.getStoreName(), request.getStart(), request.getEnd())).thenReturn(courierEntities);

        // Then
        List<Courier> result = courierService.getTravelsByCourierIdStoreNameAndTimeRange(request);

        assertFalse(result.isEmpty());
        assertEquals(couriers.get(0).getCourierId(), result.get(0).getCourierId());

        // Verify
        verify(courierRepository).findByCourierIdAndStoreNameAndTimestampBetween(request.getCourierId(), request.getStoreName(), request.getStart(), request.getEnd());

    }

    @Test
    void getTotalTravelDistance_shouldReturnTotalDistanceTraveledByCourier() {

        // Given
        String courierId = "courier1";
        LocalDateTime timestamp1 = LocalDateTime.now().minusMinutes(2);
        LocalDateTime timestamp2 = LocalDateTime.now();
        double lat1 = 37.7749;
        double lng1 = -122.4194;
        double lat2 = 37.7750;
        double lng2 = -122.4183;

        List<CourierEntity> travels = List.of(
                new CourierEntity(UUID.randomUUID().toString(), courierId, lat1, lng1, "store1", timestamp1),
                new CourierEntity(UUID.randomUUID().toString(), courierId, lat2, lng2, "store1", timestamp2)
        );

        // Calculate the distance between two points in kilometers
        double distanceInKilometers = DistanceUtils.calculateDistance(lat1, lng1, lat2, lng2, DistanceType.KILOMETERS);

        // When
        when(courierRepository.findByCourierIdOrderByTimestampAsc(courierId)).thenReturn(travels);

        // Then
        double totalDistance = courierService.getTotalTravelDistance(courierId);

        assertEquals(distanceInKilometers, totalDistance, 0.001); // Allow a small tolerance for floating point comparisons

        // Verify
        verify(courierRepository).findByCourierIdOrderByTimestampAsc(courierId);

    }

}
