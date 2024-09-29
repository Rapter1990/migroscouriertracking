package com.casestudy.migroscouriertracking.courier.repository;

import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface named {@link CourierRepository} for accessing and manipulating {@link CourierEntity} data.
 * Extends {@link JpaRepository} to provide basic CRUD operations and custom query methods.
 */
public interface CourierRepository extends JpaRepository<CourierEntity, String> {

    /**
     * Finds a list of CourierEntities associated with the specified courier ID.
     *
     * @param courierId the unique identifier of the courier
     * @return a list of CourierEntities that match the given courier ID
     */
    List<CourierEntity> findByCourierId(String courierId);

    /**
     * Finds a list of CourierEntities associated with the specified courier ID, store name,
     * and within the provided timestamp range, ordered by timestamp in descending order.
     *
     * @param courierId the unique identifier of the courier
     * @param storeName the name of the store
     * @param start     the start timestamp of the range
     * @param end       the end timestamp of the range
     * @return a list of CourierEntities that match the given criteria, ordered by timestamp descending
     */
    List<CourierEntity> findByCourierIdAndStoreNameAndTimestampBetweenOrderByTimestampDesc(String courierId, String storeName, LocalDateTime start, LocalDateTime end);

    /**
     * Finds a list of CourierEntities associated with the specified courier ID and orders them by timestamp in ascending order.
     *
     * @param courierId the unique identifier of the courier
     * @return a list of CourierEntities that match the given courier ID, ordered by timestamp
     */
    List<CourierEntity> findByCourierIdOrderByTimestampAsc(String courierId);

}
