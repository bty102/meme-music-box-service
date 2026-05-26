package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoomBookingService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).MEMBER.name())")
    public RoomBookingResponse createRoomBooking(@Valid RoomBookingCreationRequest request, Long memberAccountId);
}
