package com.casestudy.migroscouriertracking.courier.model.mapper;

import com.casestudy.migroscouriertracking.common.model.mapper.BaseMapper;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.dto.response.CourierResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourierToCourierResponseMapper extends BaseMapper<Courier, CourierResponse> {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "lat", source = "lat")
    @Mapping(target = "lng", source = "lng")
    @Mapping(target = "timestamp", source = "timestamp")
    CourierResponse map(Courier source);

    List<CourierResponse> map(List<Courier> sources);

    static CourierToCourierResponseMapper initialize() {
        return Mappers.getMapper(CourierToCourierResponseMapper.class);
    }

}
