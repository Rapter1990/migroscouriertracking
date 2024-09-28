package com.casestudy.migroscouriertracking.courier.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a store entity named {@link StoreEntity} that holds information about a store's location and creation time.
 */
@Entity
@Table(name = "stores")
@Getter
@Setter
@NoArgsConstructor
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private String id = UUID.randomUUID().toString();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Double lat;

    @Column(nullable = false)
    private Double lng;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    /**
     * Constructs a new StoreEntity with the specified parameters.
     *
     * @param name the name of the store
     * @param lat  the latitude of the store's location
     * @param lng  the longitude of the store's location
     */
    public StoreEntity(String name, Double lat, Double lng) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    /**
     * Constructs a new StoreEntity with the specified parameters including creation time.
     *
     * @param name       the name of the store
     * @param lat        the latitude of the store's location
     * @param lng        the longitude of the store's location
     * @param createdAt  the timestamp when the store was created
     */
    public StoreEntity(String name, double lat, double lng, LocalDateTime createdAt) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.createdAt=createdAt;
    }

}

