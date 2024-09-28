package com.casestudy.migroscouriertracking.courier.controller;

import com.casestudy.migroscouriertracking.common.model.dto.response.CustomResponse;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.dto.request.LogCourierLocationRequest;
import com.casestudy.migroscouriertracking.courier.model.dto.request.TravelQueryRequest;
import com.casestudy.migroscouriertracking.courier.model.dto.response.CourierResponse;
import com.casestudy.migroscouriertracking.courier.model.mapper.CourierToCourierResponseMapper;
import com.casestudy.migroscouriertracking.courier.service.CourierService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller named {@link CourierController} for managing courier operations.
 * This controller handles requests related to couriers, including logging their
 * locations, retrieving past travels, and calculating total travel distance.
 */
@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
@Validated
public class CourierController {

    private final CourierService courierService;

    private final CourierToCourierResponseMapper courierToCourierResponseMapper = CourierToCourierResponseMapper.initialize();;

    /**
     * Logs the location of a courier.
     *
     * @param logRequest the request containing the courier's location details
     * @return a CustomResponse indicating the success of the operation
     */
    @PostMapping("/log-location")
    public CustomResponse<String> logCourierLocation(
            @RequestBody @Valid LogCourierLocationRequest logRequest) {
        courierService.logCourierLocation(logRequest);
        return CustomResponse.successOf("Location logged successfully.");
    }

    /**
     * Retrieves the past travels of a courier by their ID.
     *
     * @param courierId the ID of the courier whose travels are to be retrieved
     * @return a CustomResponse containing a list of CourierResponse objects representing the courier's past travels
     */
    @GetMapping("/travels/{courierId}")
    public CustomResponse<List<CourierResponse>> getPastTravels(
            @PathVariable @UUID String courierId) {
        List<Courier> travels = courierService.getPastTravelsByCourierId(courierId);
        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);
        return CustomResponse.successOf(response);
    }

    /**
     * Retrieves travels of a courier based on courier ID, store name, and time range.
     *
     * @param request the request containing the criteria for the travel query
     * @return a CustomResponse containing a list of CourierResponse objects matching the criteria
     */
    @PostMapping("/travels")
    public CustomResponse<List<CourierResponse>> getTravelsByCourierIdStoreNameAndTimeRange(
            @RequestBody TravelQueryRequest request) {
        List<Courier> travels = courierService.getTravelsByCourierIdStoreNameAndTimeRange(request);
        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);
        return CustomResponse.successOf(response);
    }

    /**
     * Retrieves the total travel distance of a courier by their ID.
     *
     * @param courierId the ID of the courier whose total travel distance is to be retrieved
     * @return a CustomResponse containing the total travel distance formatted as a string
     */
    @GetMapping("/travels/{courierId}/total-distance")
    public CustomResponse<String> getTotalTravelDistance(
            @PathVariable @UUID String courierId) {
        double totalDistanceInKm = courierService.getTotalTravelDistance(courierId);
        String formattedDistance = String.format("%.2f km", totalDistanceInKm);
        return CustomResponse.successOf(formattedDistance);
    }

}
