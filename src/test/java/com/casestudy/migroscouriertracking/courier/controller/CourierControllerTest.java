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
        LogCourierLocationRequest logRequest = new LogCourierLocationRequest(
                "123e4567-e89b-12d3-a456-426614174000",
                37.7749,
                -122.4194,
                LocalDateTime.now()
        );

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
        String courierId = "123e4567-e89b-12d3-a456-426614174000";
        List<Courier> travels = List.of(
                new Courier("1", courierId, 37.7749, -122.4194, "store1", LocalDateTime.now()),
                new Courier("2", courierId, 37.7750, -122.4183, "store1", LocalDateTime.now().minusHours(1))
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
        TravelQueryRequest request = new TravelQueryRequest(
                "123e4567-e89b-12d3-a456-426614174000",
                "Ataşehir MMM Migros",
                LocalDateTime.parse("28/09/2024 01:58", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                LocalDateTime.parse("28/09/2024 02:00", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
        );

        List<Courier> travels = List.of(
                new Courier("1", request.getCourierId(), 37.7749, -122.4194, request.getStoreName(),
                        LocalDateTime.parse("28/09/2024 01:59", DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
        );

        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);

        // When
        when(courierService.getTravelsByCourierIdStoreNameAndTimeRange(any(TravelQueryRequest.class))).thenReturn(travels);

        // Then
        mockMvc.perform(post("/api/couriers/travels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray())
                .andExpect(jsonPath("$.response[0].storeName").value(response.get(0).getStoreName()));

        // Verify
        verify(courierService).getTravelsByCourierIdStoreNameAndTimeRange(any(TravelQueryRequest.class));

    }


    @Test
    public void getTotalTravelDistance_shouldReturnTotalDistanceTraveledByCourier() throws Exception {

        // Given
        String courierId = "123e4567-e89b-12d3-a456-426614174000";
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
