package com.casestudy.migroscouriertracking.courier.model;

import lombok.*;

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

