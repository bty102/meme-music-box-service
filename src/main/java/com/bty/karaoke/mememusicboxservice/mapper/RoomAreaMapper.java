package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoomAreaMapper {

    public RoomArea toRoomArea(RoomAreaCreationRequest request);
    public RoomAreaResponse toRoomAreaResponse(RoomArea roomArea);
    public void updateRoomArea(@MappingTarget RoomArea roomArea, RoomAreaUpdateRequest request);
}
