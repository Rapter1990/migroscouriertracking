package com.casestudy.migroscouriertracking.courier.utils.validator;

import com.casestudy.migroscouriertracking.courier.exception.TimestampAfterStoreCreationException;
import com.casestudy.migroscouriertracking.courier.model.dto.request.LogCourierLocationRequest;
import com.casestudy.migroscouriertracking.courier.model.entity.StoreEntity;
import com.casestudy.migroscouriertracking.courier.repository.StoreRepository;
import com.casestudy.migroscouriertracking.courier.utils.DistanceUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class TimestampAfterStoreCreationValidator implements ConstraintValidator<TimestampAfterStoreCreation, LogCourierLocationRequest> {

    private final StoreRepository storeRepository;

    @Override
    public void initialize(TimestampAfterStoreCreation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LogCourierLocationRequest request, ConstraintValidatorContext context) {
        // Check if the request is null or any required field is null
        if (request == null || request.getTimestamp() == null || request.getLat() == null || request.getLng() == null) {
            return false; // Invalid request
        }

        double lat = request.getLat();
        double lng = request.getLng();
        LocalDateTime timestamp = request.getTimestamp();

        // Find the nearest store and validate the timestamp
        try {
            Optional<StoreEntity> nearestStore = storeRepository.findAll().stream()
                    .filter(store -> DistanceUtils.isWithinRadius(lat, lng, store.getLat(), store.getLng(), 100.0))
                    .findFirst();

            // If a nearby store is found, validate the timestamp
            if (nearestStore.isPresent()) {
                StoreEntity store = nearestStore.get();
                if (!timestamp.isAfter(store.getCreatedAt())) {
                    throw new TimestampAfterStoreCreationException("Timestamp must be after the nearest store's creation time");
                }
            }
            // If no nearby store found or timestamp is valid, return true
            return true; // Validation passed

        } catch (Exception e) {
            // Log the error and return false for validation failure
            log.error("Error during validation: {}", e.getMessage());
            return false;
        }
    }

}
