package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomResponse;
import com.bty.karaoke.mememusicboxservice.entity.Room;
import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.RoomMapper;
import com.bty.karaoke.mememusicboxservice.repository.RoomAreaRepository;
import com.bty.karaoke.mememusicboxservice.repository.RoomRepository;
import com.bty.karaoke.mememusicboxservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final RoomAreaRepository roomAreaRepository;
    private final RoomMapper roomMapper;

    @Override
    public RoomResponse createRoom(@Valid RoomCreationRequest request) {
        if(roomRepository.existsByRoomNumber(request.getRoomNumber())) {
            throw new AppException(ErrorCode.ROOM_NUMBER_EXISTED);
        }

        RoomArea roomArea = roomAreaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new AppException(ErrorCode.AREA_NOT_EXISTED));

        if(!roomArea.getIsActive()) {
            throw new AppException(ErrorCode.AREA_OF_ROOM_NOT_ACTIVE);
        }

        Room room = roomMapper.toRoom(request);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setIsActive(true);
        room.setArea(roomArea);
        room = roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }

    @Override
    public Page<RoomResponse> findRoomsByAreaId(Long areaId, int pageNumber, int pageSize) {
        if(areaId == null) {
            throw new AppException(ErrorCode.AREA_NOT_EXISTED);
        }

        if(!roomAreaRepository.existsById(areaId)) {
            throw new AppException(ErrorCode.AREA_NOT_EXISTED);
        }

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("roomNumber").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Room> roomPage = roomRepository.findByArea_Id(areaId, pageable);
        return roomPage.map(room -> roomMapper.toRoomResponse(room));
    }

    @Override
    public Page<RoomResponse> findRoomsByAreaId(Long areaId, Boolean isActive, int pageNumber, int pageSize) {
        if(areaId == null) {
            throw new AppException(ErrorCode.AREA_NOT_EXISTED);
        }

        if(!roomAreaRepository.existsById(areaId)) {
            throw new AppException(ErrorCode.AREA_NOT_EXISTED);
        }

        if(isActive == null) {
            throw new AppException(ErrorCode.ROOM_ACTIVE_STATE_NULL);
        }

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("roomNumber").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Room> roomPage = roomRepository.findByArea_IdAndIsActive(areaId, isActive ,pageable);
        return roomPage.map(room -> roomMapper.toRoomResponse(room));
    }

    @Override
    public RoomResponse findRoomById(Long id) {
        if(id == null) {
            throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
        }
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        return roomMapper.toRoomResponse(room);
    }

    @Override
    public RoomResponse updateRoom(Long id, @Valid RoomUpdateRequest request) {
        if(id == null) {
            throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
        }
        Room room = roomRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        if(roomRepository.existsByRoomNumberAndIdIsNot(request.getRoomNumber(), id)) {
            throw new AppException(ErrorCode.ROOM_NUMBER_EXISTED);
        }

        RoomArea area = roomAreaRepository.findById(request.getAreaId())
                .orElseThrow(() -> new AppException(ErrorCode.AREA_NOT_EXISTED));

        if(request.getIsActive() && !area.getIsActive()) {
            throw new AppException(ErrorCode.AREA_OF_ROOM_NOT_ACTIVE);
        }
        if(!request.getIsActive() && !room.getStatus().equals(RoomStatus.AVAILABLE)) {
            throw new AppException(ErrorCode.ROOM_STATUS_NOT_AVAILABLE);
        }

        roomMapper.updateRoom(room, request);
        room.setArea(area);
        room = roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }

    @Override
    public Page<RoomResponse> findRoomsByRoomNumberOrCapacity(Integer roomNumber, Integer capacity, int pageNumber, int pageSize) {
        if(roomNumber == null) {
            throw new AppException(ErrorCode.ROOM_NUMBER_NULL);
        }
        if(capacity == null) {
            throw new AppException(ErrorCode.ROOM_CAPACITY_NULL);
        }

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("roomNumber").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Room> roomPage = roomRepository.findByRoomNumberOrCapacity(roomNumber, capacity, pageable);
        return roomPage.map(room -> roomMapper.toRoomResponse(room));
    }

    @Override
    public Page<RoomResponse> findRoomsByRoomNumberOrCapacity(Integer roomNumber, Integer capacity, Boolean isActive, int pageNumber, int pageSize) {

        if(roomNumber == null) {
            throw new AppException(ErrorCode.ROOM_NUMBER_NULL);
        }
        if(capacity == null) {
            throw new AppException(ErrorCode.ROOM_CAPACITY_NULL);
        }

        if(isActive == null) {
            throw new AppException(ErrorCode.ROOM_ACTIVE_STATE_NULL);
        }

        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("roomNumber").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Room> roomPage = roomRepository.findByRoomNumberAndIsActiveOrCapacityAndIsActive(roomNumber, isActive, capacity, isActive,pageable);
        return roomPage.map(room -> roomMapper.toRoomResponse(room));
    }
}
