package com.casestudy.migroscouriertracking.courier.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

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
    @PastOrPresent(message = "Start time must be in the past or present")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime start;

    @NotNull(message = "End time cannot be null")
    @FutureOrPresent(message = "End time must be in the future or present")
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime end;

    @AssertTrue(message = "Start time must be before end time")
    public boolean isValidTimeRange() {
        return start != null && end != null && start.isBefore(end);
    }

}

