package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoomAreaMapper {

    public RoomArea toRoomArea(RoomAreaCreationRequest request);
    public RoomAreaResponse toRoomAreaResponse(RoomArea roomArea);
}
