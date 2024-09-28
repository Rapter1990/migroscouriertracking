package com.casestudy.migroscouriertracking.courier.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents a domain object named {@link Courier} with relevant details including location and associated store.
 * This class serves as a data model for tracking courier information.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Courier {

    private String id;
    private String courierId;
    private Double lat;
    private Double lng;
    private String storeName;
    private LocalDateTime timestamp;

}

