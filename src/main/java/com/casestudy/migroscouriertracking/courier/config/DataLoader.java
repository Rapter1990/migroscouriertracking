package com.casestudy.migroscouriertracking.courier.config;

import com.casestudy.migroscouriertracking.courier.model.entity.StoreEntity;
import com.casestudy.migroscouriertracking.courier.repository.StoreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * DataLoader class for loading initial data into the database.
 */
@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final StoreRepository storeRepository;

    @Bean
    public CommandLineRunner loadInitialData() {
        return args -> {
            List<StoreEntity> stores = Arrays.asList(
                    new StoreEntity("Ataşehir MMM Migros", 40.9923307, 29.1244229),
                    new StoreEntity("Novada MMM Migros", 40.986106, 29.1161293),
                    new StoreEntity("Beylikdüzü 5M Migros", 41.0066851, 28.6552262),
                    new StoreEntity("Ortaköy MMM Migros", 41.055783, 29.0210292),
                    new StoreEntity("Caddebostan MMM Migros", 40.9632463, 29.0630908)
            );
            storeRepository.saveAll(stores);
        };
    }

}
