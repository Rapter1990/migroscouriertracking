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

/**
 * Validator for the {@link TimestampAfterStoreCreation} annotation that checks
 * if a given timestamp is after the creation time of the nearest store.
 *
 * @see #isValid(LogCourierLocationRequest, ConstraintValidatorContext)
 * @see #initialize(TimestampAfterStoreCreation)
 */
@RequiredArgsConstructor
@Slf4j
public class TimestampAfterStoreCreationValidator implements ConstraintValidator<TimestampAfterStoreCreation, LogCourierLocationRequest> {

    private final StoreRepository storeRepository;

    /**
     * Initializes the validator with the specified {@link TimestampAfterStoreCreation} constraint annotation.
     **/
    @Override
    public void initialize(TimestampAfterStoreCreation constraintAnnotation) {
    }

    /**
     * Validates the given {@link LogCourierLocationRequest} to ensure that the
     * timestamp is after the creation time of the nearest store within a 100-meter radius.
     *
     * @param request the {@link LogCourierLocationRequest} object containing the timestamp, latitude, and longitude
     * @param context the {@link ConstraintValidatorContext} used for building error messages
     * @return true if the timestamp is valid (i.e., after the nearest store's creation time or no nearby store found),
     *         false if the request is null or if any required field is missing
     */
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
