package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import com.bty.karaoke.mememusicboxservice.entity.RoomBooking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = {
                RoomMapper.class,
                AccountMapper.class
        }
)
public interface RoomBookingMapper {

    public RoomBooking toRoomBooking(RoomBookingCreationRequest request);

    public RoomBookingResponse toRoomBookingResponse(RoomBooking roomBooking);
}
