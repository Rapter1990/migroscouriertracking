package com.casestudy.migroscouriertracking.courier.model;

import lombok.*;

/**
 * Represents a domain object named {@link Location} with latitude and longitude coordinates.
 * This class can be used for various location-based functionalities within the application.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Location {

    private Double latitude;
    private Double longitude;

}

