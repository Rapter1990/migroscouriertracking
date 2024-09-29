package com.casestudy.migroscouriertracking.courier.model.dto.request;

import com.casestudy.migroscouriertracking.courier.utils.validator.TimestampAfterStoreCreation;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a request named {@link LogCourierLocationRequest} to log the location of a courier.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TimestampAfterStoreCreation(message = "Timestamp must be after the nearest store's creation time")
public class LogCourierLocationRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String courierId;

    @DecimalMin(value = "-90.0", message = "Latitude must be between -90 and 90")
    @DecimalMax(value = "90.0", message = "Latitude must be between -90 and 90")
    private Double lat;

    @DecimalMin(value = "-180.0", message = "Longitude must be between -180 and 180")
    @DecimalMax(value = "180.0", message = "Longitude must be between -180 and 180")
    private Double lng;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime timestamp;

}

