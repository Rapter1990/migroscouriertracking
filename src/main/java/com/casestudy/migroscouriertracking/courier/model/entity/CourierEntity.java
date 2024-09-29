package com.casestudy.migroscouriertracking.courier.model.entity;

import jakarta.persistence.*;
import lombok.*;

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
@AllArgsConstructor
@Builder
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

}

