package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public interface RoomService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public RoomResponse createRoom(@Valid RoomCreationRequest request);

    /**
     *
     * @param areaId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public Page<RoomResponse> findRoomsByAreaId(Long areaId, int pageNumber, int pageSize);

    @PreAuthorize("""
        (
            @roomAreaRepository.existsByIdAndIsActive(#areaId, true)
            and #isActive == true
        )
        or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
    """)
    public Page<RoomResponse> findRoomsByAreaId(Long areaId, Boolean isActive ,int pageNumber, int pageSize);

    @PostAuthorize("returnObject.isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public RoomResponse findRoomById(Long id);

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public RoomResponse updateRoom(Long id, @Valid RoomUpdateRequest request);

    /**
     *
     * @param roomNumber
     * @param capacity
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public Page<RoomResponse> findRoomsByRoomNumberOrCapacity(Integer roomNumber, Integer  capacity, int pageNumber, int pageSize);

    /**
     *
     * @param roomNumber
     * @param capacity
     * @param isActive
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("#isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public Page<RoomResponse> findRoomsByRoomNumberOrCapacity(Integer roomNumber, Integer  capacity, Boolean isActive, int pageNumber, int pageSize);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public void openRoom(Long roomId, Long creatorAccountId, Long memberAccountId);
}
