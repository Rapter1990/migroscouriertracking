package com.casestudy.migroscouriertracking.courier.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

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

}

