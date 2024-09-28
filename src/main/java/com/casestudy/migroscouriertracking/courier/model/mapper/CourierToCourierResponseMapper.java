package com.casestudy.migroscouriertracking.courier.model.mapper;

import com.casestudy.migroscouriertracking.common.model.mapper.BaseMapper;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.dto.response.CourierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Mapper interface for converting {@link Courier} to {@link CourierResponse}.
 * This interface defines the mapping between the Courier and CourierResponse classes,
 * facilitating the transformation of data for API responses.
 */
@Mapper
public interface CourierToCourierResponseMapper extends BaseMapper<Courier, CourierResponse> {

    /**
     * Maps a single {@link Courier} to a {@link CourierResponse}.
     *
     * @param source the {@link Courier} to map
     * @return the mapped {@link CourierResponse}
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "lat", source = "lat")
    @Mapping(target = "lng", source = "lng")
    @Mapping(target = "timestamp", source = "timestamp")
    CourierResponse map(Courier source);

    /**
     * Maps a list of {@link Courier} to a list of {@link CourierResponse}.
     *
     * @param sources the list of {@link Courier} to map
     * @return the list of mapped {@link CourierResponse}
     */
    List<CourierResponse> map(List<Courier> sources);

    /**
     * Initializes and returns an instance of {@link CourierToCourierResponseMapper}.
     *
     * @return a new instance of {@link CourierToCourierResponseMapper}
     */
    static CourierToCourierResponseMapper initialize() {
        return Mappers.getMapper(CourierToCourierResponseMapper.class);
    }

}
