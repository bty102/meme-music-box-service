package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    /**
     *
     * @param roomId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("""
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
            """)
    public Page<RoomBookingResponse> getRoomBookingsOfRoom(Long roomId, int  pageNumber, int pageSize);

    /**
     *
     * @param memberAccountId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<RoomBookingResponse> getRoomBookingsOfMember(Long memberAccountId, int pageNumber, int pageSize);


    @PreAuthorize("""
                @roomBookingRepository.existsByIdAndMemberAccount_Email(#roomBookingId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
            """)
    public RoomBookingResponse getRoomBookingById(Long roomBookingId);
}
