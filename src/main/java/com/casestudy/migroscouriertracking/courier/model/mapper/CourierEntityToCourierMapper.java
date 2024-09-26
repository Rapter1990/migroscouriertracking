package com.casestudy.migroscouriertracking.courier.model.mapper;

import com.casestudy.migroscouriertracking.common.model.mapper.BaseMapper;
import com.casestudy.migroscouriertracking.courier.model.Courier;
import com.casestudy.migroscouriertracking.courier.model.entity.CourierEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CourierEntityToCourierMapper extends BaseMapper<CourierEntity, Courier> {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "courierId", source = "courierId")
    @Mapping(target = "lat", source = "lat")
    @Mapping(target = "lng", source = "lng")
    @Mapping(target = "storeName", source = "storeName")
    @Mapping(target = "timestamp", source = "timestamp")
    Courier map(CourierEntity source);

    List<Courier> map(List<CourierEntity> sources);

    static CourierEntityToCourierMapper initialize() {
        return Mappers.getMapper(CourierEntityToCourierMapper.class);
    }

}
