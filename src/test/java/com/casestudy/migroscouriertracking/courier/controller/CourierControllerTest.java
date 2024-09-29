package com.casestudy.migroscouriertracking.courier.controller;

import com.casestudy.migroscouriertracking.base.AbstractRestControllerTest;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.dto.request.LogCourierLocationRequest;
import com.casestudy.migroscouriertracking.courier.model.dto.request.TravelQueryRequest;
import com.casestudy.migroscouriertracking.courier.model.dto.response.CourierResponse;
import com.casestudy.migroscouriertracking.courier.model.mapper.CourierToCourierResponseMapper;
import com.casestudy.migroscouriertracking.courier.service.CourierService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for the {@link CourierController} class, verifying the functionality
 * of endpoints related to logging courier locations and retrieving travel data.
 */
class CourierControllerTest extends AbstractRestControllerTest {

    @MockBean
    private CourierService courierService;

    private final CourierToCourierResponseMapper courierToCourierResponseMapper = CourierToCourierResponseMapper.initialize();

    @Test
    public void logCourierLocation_shouldReturnSuccessMessage() throws Exception {

        // Given
        LogCourierLocationRequest logRequest = LogCourierLocationRequest.builder()
                .courierId(UUID.randomUUID().toString())
                .lat(37.7749)
                .lng(-122.4194)
                .timestamp(LocalDateTime.now().plusMinutes(1))
                .build();

        // When
        doNothing().when(courierService).logCourierLocation(any());

        // Then
        mockMvc.perform(post("/api/couriers/log-location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value("Location logged successfully."));

        // Verify
        verify(courierService).logCourierLocation(any());

    }


    @Test
    public void getPastTravels_shouldReturnListOfTravels() throws Exception {

        // Given
        String courierId = UUID.randomUUID().toString();

        List<Courier> travels = List.of(
                Courier.builder()
                        .id(UUID.randomUUID().toString())
                        .courierId(courierId)
                        .lat(37.7749)
                        .lng(-122.4194)
                        .storeName("store1")
                        .timestamp(LocalDateTime.now())
                        .build(),
                Courier.builder()
                        .id(UUID.randomUUID().toString())
                        .courierId(courierId)
                        .lat(37.7750)
                        .lng(-122.4183)
                        .storeName("store1")
                        .timestamp(LocalDateTime.now().minusHours(1))
                        .build()
        );

        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);

        // When
        when(courierService.getPastTravelsByCourierId(courierId)).thenReturn(travels);

        // Then
        mockMvc.perform(get("/api/couriers/travels/{courierId}", courierId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response[0].storeName").value(response.get(0).getStoreName()))
                .andExpect(jsonPath("$.response[1].storeName").value(response.get(1).getStoreName()));

        // Verify
        verify(courierService).getPastTravelsByCourierId(courierId);

    }


    @Test
    public void getTravelsByCourierIdStoreNameAndTimeRange_shouldReturnFilteredTravels() throws Exception {

        // Given
        String courierId = UUID.randomUUID().toString();
        TravelQueryRequest request = TravelQueryRequest.builder()
                .storeName("Ataşehir MMM Migros")
                .start(LocalDateTime.parse("28/09/2024 01:58", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .end(LocalDateTime.parse("28/09/2024 02:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .build();

        List<Courier> travels = List.of(
                Courier.builder()
                        .id(UUID.randomUUID().toString())
                        .courierId(courierId)
                        .lat(37.7749)
                        .lng(-122.4194)
                        .storeName(request.getStoreName())
                        .timestamp(LocalDateTime.parse("28/09/2024 01:59", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                        .build()
        );

        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);

        // When
        when(courierService.getTravelsByCourierIdStoreNameAndTimeRange(eq(courierId),any(TravelQueryRequest.class))).thenReturn(travels);

        // Then
        mockMvc.perform(post("/api/couriers/travels/{courierId}", courierId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response[0].storeName").value(response.get(0).getStoreName()));

        // Verify
        verify(courierService).getTravelsByCourierIdStoreNameAndTimeRange(eq(courierId),any(TravelQueryRequest.class));

    }


    @Test
    public void getTotalTravelDistance_shouldReturnTotalDistanceTraveledByCourier() throws Exception {

        // Given
        String courierId = UUID.randomUUID().toString();
        double totalDistance = 0.097; // Example distance in kilometers
        String formattedDistance = String.format("%.2f km", totalDistance);

        // When
        when(courierService.getTotalTravelDistance(courierId)).thenReturn(totalDistance);

        // Then
        mockMvc.perform(get("/api/couriers/travels/{courierId}/total-distance", courierId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").value(formattedDistance));

        // Verify
        verify(courierService).getTotalTravelDistance(courierId);

    }

}
