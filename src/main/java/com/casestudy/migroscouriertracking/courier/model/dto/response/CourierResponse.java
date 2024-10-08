package com.casestudy.migroscouriertracking.courier.model.dto.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents the response named {@link CourierResponse} containing information about a courier's location and associated details.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourierResponse {

    private String id;
    private Double lat;
    private Double lng;
    private String storeName;
    private LocalDateTime timestamp;

}

