package com.casestudy.migroscouriertracking.courier.utils.validator;

import com.casestudy.migroscouriertracking.courier.model.dto.request.LogCourierLocationRequest;
import com.casestudy.migroscouriertracking.courier.model.entity.StoreEntity;
import com.casestudy.migroscouriertracking.courier.repository.StoreRepository;
import com.casestudy.migroscouriertracking.courier.utils.DistanceUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TimestampAfterStoreCreationValidator implements ConstraintValidator<TimestampAfterStoreCreation, LogCourierLocationRequest> {

    private final StoreRepository storeRepository;

    @Override
    public void initialize(TimestampAfterStoreCreation constraintAnnotation) {
    }

    @Override
    public boolean isValid(LogCourierLocationRequest request, ConstraintValidatorContext context) {
        double lat = request.getLat();
        double lng = request.getLng();
        LocalDateTime timestamp = request.getTimestamp();


        // Find the first store within 100 meters radius of the courier's location
        Optional<StoreEntity> nearestStore = storeRepository.findAll().stream()
                .filter(store -> DistanceUtils.isWithinRadius(lat, lng, store.getLat(), store.getLng(), 100.0))
                .findFirst();

        // If a nearby store is found, validate the timestamp
        return nearestStore.map(store -> timestamp.isAfter(store.getCreatedAt()))
                .orElse(false);  // No nearby store found, validation fails

    }

}
