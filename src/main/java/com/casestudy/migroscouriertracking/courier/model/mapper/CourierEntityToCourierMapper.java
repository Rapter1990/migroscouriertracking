package com.casestudy.migroscouriertracking.courier.model.mapper;

import com.casestudy.migroscouriertracking.common.model.mapper.BaseMapper;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting {@link CourierEntity} to {@link Courier}.
 * This interface defines the mapping between the CourierEntity and Courier classes,
 * allowing for easy transformation of data between these two representations.
 */
@Mapper
public interface CourierEntityToCourierMapper extends BaseMapper<CourierEntity, Courier> {

    /**
     * Maps a single {@link CourierEntity} to a {@link Courier}.
     *
     * @param source the {@link CourierEntity} to map
     * @return the mapped {@link Courier}
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "courierId", source = "courierId")
    @Mapping(target = "lat", source = "lat")
    @Mapping(target = "lng", source = "lng")
    @Mapping(target = "storeName", source = "storeName")
    @Mapping(target = "timestamp", source = "timestamp")
    Courier map(CourierEntity source);

    /**
     * Maps a list of {@link CourierEntity} to a list of {@link Courier}.
     *
     * @param sources the list of {@link CourierEntity} to map
     * @return the list of mapped {@link Courier}
     */
    List<Courier> map(List<CourierEntity> sources);

    /**
     * Initializes and returns an instance of {@link CourierEntityToCourierMapper}.
     *
     * @return a new instance of {@link CourierEntityToCourierMapper}
     */
    static CourierEntityToCourierMapper initialize() {
        return Mappers.getMapper(CourierEntityToCourierMapper.class);
    }

}
