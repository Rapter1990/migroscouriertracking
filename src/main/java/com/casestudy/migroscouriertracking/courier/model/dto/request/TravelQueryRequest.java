package com.casestudy.migroscouriertracking.courier.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a request named {@link TravelQueryRequest} for querying courier travel details based on various criteria.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TravelQueryRequest {

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String courierId;

    @NotBlank(message = "Store name cannot be blank")
    private String storeName;

    @NotNull(message = "Start time cannot be null")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime start;

    @NotNull(message = "End time cannot be null")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime end;

    /**
     * Validates that the start time is before the end time.
     *
     * @return true if the start time is before the end time; false otherwise.
     */
    @AssertTrue(message = "Start time must be before end time")
    public boolean isValidTimeRange() {
        return start != null && end != null && start.isBefore(end);
    }

}

