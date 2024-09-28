package com.casestudy.migroscouriertracking.courier.repository;

import com.casestudy.migroscouriertracking.courier.model.entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface named {@link StoreRepository} for accessing and manipulating {@link StoreEntity} data.
 * Extends {@link JpaRepository} to provide basic CRUD operations.
 */
public interface StoreRepository extends JpaRepository<StoreEntity, String> {

}
