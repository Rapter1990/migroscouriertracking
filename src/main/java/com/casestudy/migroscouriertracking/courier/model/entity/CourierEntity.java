package com.casestudy.migroscouriertracking.courier.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a courier entity named {@link CourierEntity} that holds information about a courier's location and associated data.
 */
@Entity
@Table(name = "couriers")
@Getter
@Setter
@NoArgsConstructor
public class CourierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id = UUID.randomUUID().toString();

    @Column(name = "courier_id", nullable = false)
    private String courierId;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(name = "store_name", nullable = false)
    private String storeName;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * Constructs a new CourierEntity with the specified parameters.
     *
     * @param courierId  the ID of the courier
     * @param lat        the latitude of the courier's location
     * @param lng        the longitude of the courier's location
     * @param storeName  the name of the store associated with the courier's location
     * @param timestamp  the timestamp of the location record
     */
    public CourierEntity(String courierId, double lat, double lng,
                         String storeName, LocalDateTime timestamp) {
        this.courierId = courierId;
        this.lat = lat;
        this.lng = lng;
        this.storeName = storeName;
        this.timestamp = timestamp;
    }

    /**
     * Constructs a new CourierEntity with the specified parameters including an ID.
     *
     * @param id         the unique identifier for the courier
     * @param courierId  the ID of the courier
     * @param lat        the latitude of the courier's location
     * @param lng        the longitude of the courier's location
     * @param name       the name of the store associated with the courier's location
     * @param timestamp  the timestamp of the location record
     */
    public CourierEntity(String id, String courierId, double lat, double lng, String name, LocalDateTime timestamp) {
        this.id=id;
        this.courierId = courierId;
        this.lat = lat;
        this.lng = lng;
        this.storeName = name;
        this.timestamp = timestamp;
    }

}

