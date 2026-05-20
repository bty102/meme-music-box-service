package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface RoomService {

    public RoomResponse createRoom(@Valid RoomCreationRequest request);

    /**
     *
     * @param areaId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<RoomResponse> findRoomsByAreaId(Long areaId, int pageNumber, int pageSize);

    public RoomResponse findRoomById(Long id);
}
