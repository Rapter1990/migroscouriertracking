package com.casestudy.migroscouriertracking.courier.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

