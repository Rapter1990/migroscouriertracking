package com.casestudy.migroscouriertracking.courier.model.mapper;

import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.dto.response.CourierResponse;
import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CourierToCourierResponseMapperTest {

    private final CourierToCourierResponseMapper mapper = CourierToCourierResponseMapper.initialize();

    @Test
    void testMapCourierCollectionNull() {

        List<CourierResponse> result = mapper.map((Collection<Courier>) null);
        assertNull(result);

    }

    @Test
    void testMapCourierListNull() {

        List<CourierResponse> result = mapper.map((List<Courier>) null);
        assertNull(result);

    }

    @Test
    void testMapCourierListEmpty() {

        List<CourierResponse> result = mapper.map(Collections.emptyList());
        assertNotNull(result);
        assertTrue(result.isEmpty());

    }

    @Test
    void testMapCourierListWithNullElements() {

        List<Courier> couriers = Arrays.asList(new Courier(), null);
        List<CourierResponse> result = mapper.map(couriers);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNull(result.get(1));

    }


    @Test
    void testMapSingleCourier() {

        Courier courier = Courier.builder()
                .id(UUID.randomUUID().toString())
                .courierId("courier123")
                .lat(47.0)
                .lng(8.0)
                .storeName("Migros")
                .timestamp(LocalDateTime.now())
                .build();

        CourierResponse result = mapper.map(courier);

        assertNotNull(result);
        assertEquals(courier.getId(), result.getId());
        assertEquals(courier.getLat(), result.getLat());
        assertEquals(courier.getLng(), result.getLng());
        assertEquals(courier.getStoreName(), result.getStoreName());
        assertEquals(courier.getTimestamp(), result.getTimestamp());

    }

    @Test
    void testMapWithEdgeCaseValues() {

        Courier courier = Courier.builder()
                .lat(Double.MAX_VALUE)
                .lng(Double.MIN_VALUE)
                .timestamp(LocalDateTime.of(2024, 9, 29, 16, 30))
                .build();

        CourierResponse result = mapper.map(courier);

        assertNotNull(result);
        assertEquals(Double.MAX_VALUE, result.getLat());
        assertEquals(Double.MIN_VALUE, result.getLng());
        assertEquals(LocalDateTime.of(2024, 9, 29, 16, 30), result.getTimestamp());

    }

}