package com.casestudy.migroscouriertracking.courier.config;

import com.casestudy.migroscouriertracking.courier.model.entity.StoreEntity;
import com.casestudy.migroscouriertracking.courier.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * DataLoader class for loading initial data into the database.
 */
@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final StoreRepository storeRepository;

    /**
     * Loads initial store data into the database when the application starts.
     *
     * @return a CommandLineRunner that loads a predefined list of StoreEntity
     *         instances into the StoreRepository.
     */
    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            List<StoreEntity> stores = Arrays.asList(
                    StoreEntity.builder()
                            .name("Ataşehir MMM Migros")
                            .lat(40.9923307)
                            .lng(29.1244229)
                            .build(),
                    StoreEntity.builder()
                            .name("Novada MMM Migros")
                            .lat(40.986106)
                            .lng(29.1161293)
                            .build(),
                    StoreEntity.builder()
                            .name("Beylikdüzü 5M Migros")
                            .lat(41.0066851)
                            .lng(28.6552262)
                            .build(),
                    StoreEntity.builder()
                            .name("Ortaköy MMM Migros")
                            .lat(41.055783)
                            .lng(29.0210292)
                            .build(),
                    StoreEntity.builder()
                            .name("Caddebostan MMM Migros")
                            .lat(40.9632463)
                            .lng(29.0630908)
                            .build()
            );
            storeRepository.saveAll(stores);
        };
    }

}
