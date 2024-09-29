package com.casestudy.migroscouriertracking.courier.model.mapper;

import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link CourierEntityToCourierMapper} that validates the mapping
 * between {@link CourierEntity} and {@link Courier} objects, ensuring correct
 * functionality for various scenarios.
 */
class CourierEntityToCourierMapperTest {

    private final CourierEntityToCourierMapper mapper = CourierEntityToCourierMapper.initialize();

    @Test
    void testMapCourierEntityCollectionNull() {

        List<Courier> result = mapper.map((Collection<CourierEntity>) null);
        assertNull(result);

    }

    @Test
    void testMapCourierEntityListNull() {

        List<Courier> result = mapper.map((List<CourierEntity>) null);
        assertNull(result);
    }

    @Test
    void testMapCourierEntityListEmpty() {

        List<Courier> result = mapper.map(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }


    @Test
    void testMapCourierEntityListWithNullElements() {

        List<CourierEntity> courierEntities = Arrays.asList(new CourierEntity(), null);
        List<Courier> result = mapper.map(courierEntities);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(1));

    }


    @Test
    void testMapSingleCourierEntity() {

        CourierEntity courierEntity = CourierEntity.builder()
                .id(UUID.randomUUID().toString())
                .courierId(UUID.randomUUID().toString())
                .lat(47.0)
                .lng(8.0)
                .storeName("Migros")
                .timestamp(LocalDateTime.now())
                .build();

        Courier result = mapper.map(courierEntity);

        assertNotNull(result);
        assertEquals(courierEntity.getId(), result.getId());
        assertEquals(courierEntity.getCourierId(), result.getCourierId());
        assertEquals(courierEntity.getLat(), result.getLat());
        assertEquals(courierEntity.getLng(), result.getLng());
        assertEquals(courierEntity.getStoreName(), result.getStoreName());
        assertEquals(courierEntity.getTimestamp(), result.getTimestamp());

    }

    @Test
    void testMapWithEdgeCaseValues() {

        CourierEntity courierEntity = CourierEntity.builder()
                .lat(Double.MAX_VALUE)
                .lng(Double.MIN_VALUE)
                .timestamp(LocalDateTime.of(2024, 9, 29, 16, 30))
                .build();

        Courier result = mapper.map(courierEntity);

        assertNotNull(result);
        assertEquals(Double.MAX_VALUE, result.getLat());
        assertEquals(Double.MIN_VALUE, result.getLng());
        assertEquals(LocalDateTime.of(2024, 9, 29, 16, 30), result.getTimestamp());

    }

}