package com.casestudy.migroscouriertracking.courier.service;

import com.casestudy.migroscouriertracking.base.AbstractBaseServiceTest;
import com.casestudy.migroscouriertracking.courier.exception.StoreFarAwayException;
import com.casestudy.migroscouriertracking.courier.exception.StoreReentryTooSoonException;
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
import static org.mockito.ArgumentMatchers.eq;
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
        String courierId = UUID.randomUUID().toString();
        double lat = 37.7749;
        double lng = -122.4194;
        LocalDateTime timestamp = LocalDateTime.now();

        LogCourierLocationRequest logRequest = LogCourierLocationRequest.builder()
                .courierId(courierId)
                .lat(lat)
                .lng(lng)
                .timestamp(timestamp)
                .build();

        StoreEntity store = StoreEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("store1")
                .lat(37.7750)
                .lng(-122.4183)
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .build();

        CourierEntity courierEntity = CourierEntity.builder()
                .id(UUID.randomUUID().toString())
                .courierId(courierId)
                .lat(lat)
                .lng(lng)
                .storeName(store.getName())
                .timestamp(timestamp)
                .build();

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
        String courierId = UUID.randomUUID().toString();
        double lat = 37.7749;
        double lng = -122.4194;
        LocalDateTime timestamp = LocalDateTime.now();

        LogCourierLocationRequest logRequest = LogCourierLocationRequest.builder()
                .courierId(courierId)
                .lat(lat)
                .lng(lng)
                .timestamp(timestamp)
                .build();

        StoreEntity store = StoreEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("store1")
                .lat(37.7750)
                .lng(-122.4183)
                .createdAt(LocalDateTime.now().plusMinutes(10))
                .build();


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
        String courierId = UUID.randomUUID().toString();

        LogCourierLocationRequest logRequest = LogCourierLocationRequest.builder()
                .courierId(courierId)
                .lat(37.7749)
                .lng(-122.4194)
                .timestamp(LocalDateTime.now())
                .build();

        StoreEntity store = StoreEntity.builder()
                .name("store1")
                .lat(37.7800)
                .lng(-122.4200)
                .createdAt(LocalDateTime.now().minusMinutes(10))
                .build();

        // When
        when(storeRepository.findAll()).thenReturn(List.of(store));

        // Then
        assertThrows(StoreFarAwayException.class, () -> courierService.logCourierLocation(logRequest));

        // Verify
        verify(storeRepository).findAll();

    }

    @Test
    void logCourierLocation_shouldThrowStoreReentryTooSoonException_ifReenteringSameStoreTooSoon() {

        // Given
        String courierId = UUID.randomUUID().toString();
        double lat = 37.7749;
        double lng = -122.4194;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastEntryTimestamp = now.minusSeconds(30); // Last entry within 30 seconds

        LogCourierLocationRequest logRequest = LogCourierLocationRequest.builder()
                .courierId(courierId)
                .lat(lat)
                .lng(lng)
                .timestamp(now)
                .build();

        StoreEntity store = StoreEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("store1")
                .lat(37.7750)
                .lng(-122.4183)
                .createdAt(now.minusMinutes(10))
                .build();

        CourierEntity lastTravelEntry = CourierEntity.builder()
                .id(UUID.randomUUID().toString())
                .courierId(courierId)
                .lat(lat)
                .lng(lng)
                .storeName(store.getName())
                .timestamp(lastEntryTimestamp)
                .build();

        // When
        when(storeRepository.findAll()).thenReturn(List.of(store));
        when(courierRepository.findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(
                eq(courierId),
                eq(store.getName()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        )).thenReturn(List.of(lastTravelEntry));

        // Then
        assertThrows(StoreReentryTooSoonException.class, () -> courierService.logCourierLocation(logRequest));

        // Verify
        verify(storeRepository).findAll();
        verify(courierRepository).findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(
                eq(courierId),
                eq(store.getName()),
                any(LocalDateTime.class),
                any(LocalDateTime.class)
        );

    }

    @Test
    void getPastTravelsByCourierId_shouldReturnTravelsForGivenCourierId() {

        // Given
        String courierId = UUID.randomUUID().toString();

        List<CourierEntity> courierEntities = List.of(CourierEntity.builder()
                .id(UUID.randomUUID().toString())
                .courierId(courierId)
                .lat(37.7749)
                .lng(-122.4194)
                .storeName("store1")
                .timestamp(LocalDateTime.now())
                .build());

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
    void getTravelsByCourierIdAndTravelQueryRequest_shouldReturnTravelsWithinTimeRange() {

        // Given
        String courierId = UUID.randomUUID().toString();

        TravelQueryRequest request = TravelQueryRequest.builder()
                .storeName("store1")
                .start(LocalDateTime.now().minusHours(1))
                .end(LocalDateTime.now())
                .build();

        List<CourierEntity> courierEntities = List.of(CourierEntity.builder()
                .id(UUID.randomUUID().toString())
                .courierId(courierId)
                .lat(37.7749)
                .lng(-122.4194)
                .storeName("store1")
                .timestamp(LocalDateTime.now())
                .build());

        List<Courier> couriers = courierEntityToCourierMapper.map(courierEntities);

        // When
        when(courierRepository.findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(courierId,
                request.getStoreName(),
                request.getStart(),
                request.getEnd())).thenReturn(courierEntities);

        // Then
        List<Courier> result = courierService.getTravelsByCourierIdStoreNameAndTimeRange(courierId,request);

        assertFalse(result.isEmpty());
        assertEquals(couriers.get(0).getCourierId(), result.get(0).getCourierId());

        // Verify
        verify(courierRepository).findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(courierId,
                request.getStoreName(),
                request.getStart(),
                request.getEnd());

    }

    @Test
    void getTotalTravelDistance_shouldReturnTotalDistanceTraveledByCourier() {

        // Given
        String courierId = UUID.randomUUID().toString();
        LocalDateTime timestamp1 = LocalDateTime.now().minusMinutes(2);
        LocalDateTime timestamp2 = LocalDateTime.now();
        double lat1 = 37.7749;
        double lng1 = -122.4194;
        double lat2 = 37.7750;
        double lng2 = -122.4183;

        List<CourierEntity> travels = List.of(
                CourierEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .courierId(courierId)
                        .lat(lat1)
                        .lng(lng1)
                        .storeName("store1")
                        .timestamp(timestamp1)
                        .build(),
                CourierEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .courierId(courierId)
                        .lat(lat2)
                        .lng(lng2)
                        .storeName("store1")
                        .timestamp(timestamp2)
                        .build()
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
