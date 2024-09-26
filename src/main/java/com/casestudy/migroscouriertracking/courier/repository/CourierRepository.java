package com.casestudy.migroscouriertracking.courier.repository;

import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CourierRepository extends JpaRepository<CourierEntity, String> {

    List<CourierEntity> findByCourierId(String courierId);

    List<CourierEntity> findByCourierIdAndStoreNameAndTimestampBetween(String courierId, String storeName, LocalDateTime start, LocalDateTime end);

    List<CourierEntity> findByCourierIdOrderByTimestampAsc(String courierId);

}
