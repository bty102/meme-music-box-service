package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RoomAreaService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public RoomAreaResponse createRoomArea(@Valid RoomAreaCreationRequest request);

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public List<RoomAreaResponse> getRoomAreas();

    @PreAuthorize("#isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public List<RoomAreaResponse> getRoomAreas(boolean isActive);

    @PostAuthorize("returnObject.isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public RoomAreaResponse getRoomAreaById(Long id);

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public RoomAreaResponse updateRoomArea(Long id, @Valid RoomAreaUpdateRequest request);
}
