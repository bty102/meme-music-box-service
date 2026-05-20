package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface RoomAreaService {

    public RoomAreaResponse createRoomArea(@Valid RoomAreaCreationRequest request);

    public List<RoomAreaResponse> getRoomAreas();

    public List<RoomAreaResponse> getRoomAreas(boolean isActive);

    public RoomAreaResponse getRoomAreaById(Long id);

    public RoomAreaResponse updateRoomArea(Long id, @Valid RoomAreaUpdateRequest request);
}
