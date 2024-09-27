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

@RestController
@RequestMapping("/api/couriers")
@RequiredArgsConstructor
@Validated
public class CourierController {

    private final CourierService courierService;

    private final CourierToCourierResponseMapper courierToCourierResponseMapper = CourierToCourierResponseMapper.initialize();;

    @PostMapping("/log-location")
    public CustomResponse<String> logCourierLocation(
            @RequestBody @Valid LogCourierLocationRequest logRequest) {
        courierService.logCourierLocation(logRequest);
        return CustomResponse.successOf("Location logged successfully.");
    }

    @GetMapping("/travels/{courierId}")
    public CustomResponse<List<CourierResponse>> getPastTravels(
            @PathVariable @UUID String courierId) {
        List<Courier> travels = courierService.getPastTravelsByCourierId(courierId);
        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);
        return CustomResponse.successOf(response);
    }

    @PostMapping("/travels")
    public CustomResponse<List<CourierResponse>> getTravelsByCourierIdStoreNameAndTimeRange(
            @RequestBody TravelQueryRequest request) {
        List<Courier> travels = courierService.getTravelsByCourierIdStoreNameAndTimeRange(request);
        List<CourierResponse> response = courierToCourierResponseMapper.map(travels);
        return CustomResponse.successOf(response);
    }

    @GetMapping("/travels/{courierId}/total-distance")
    public CustomResponse<String> getTotalTravelDistance(
            @PathVariable @UUID String courierId) {
        double totalDistanceInKm = courierService.getTotalTravelDistance(courierId);
        String formattedDistance = String.format("%.2f km", totalDistanceInKm);
        return CustomResponse.successOf(formattedDistance);
    }

}
