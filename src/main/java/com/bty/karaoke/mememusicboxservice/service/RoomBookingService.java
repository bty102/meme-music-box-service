package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoomBookingService {

    //    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).MEMBER.name())")
    public RoomBookingResponse createRoomBooking(@Valid RoomBookingCreationRequest request, Long memberAccountId);

    @PreAuthorize("""
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
            """)
    public RoomBookingResponse checkIn(Long roomBookingId, Long creatorAccountId);

    @PreAuthorize("""
                @roomBookingRepository.existsByIdAndMemberAccount_Email(#roomBookingId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
            """)
    public RoomBookingResponse cancelRoomBooking(Long roomBookingId);
}
