package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomResponse;
import com.bty.karaoke.mememusicboxservice.entity.Room;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = {RoomAreaMapper.class}
)
public interface RoomMapper {

    public Room toRoom(RoomCreationRequest request);

    public RoomResponse toRoomResponse(Room room);
}
