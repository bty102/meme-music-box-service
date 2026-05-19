package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import jakarta.validation.Valid;

public interface RoomAreaService {

    public RoomAreaResponse createRoomArea(@Valid RoomAreaCreationRequest request);
}
