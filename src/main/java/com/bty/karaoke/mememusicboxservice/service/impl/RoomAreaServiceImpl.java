package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.RoomAreaMapper;
import com.bty.karaoke.mememusicboxservice.repository.RoomAreaRepository;
import com.bty.karaoke.mememusicboxservice.repository.RoomRepository;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class RoomAreaServiceImpl implements RoomAreaService {

    private final RoomAreaRepository roomAreaRepository;
    private final RoomAreaMapper roomAreaMapper;
    private final RoomRepository roomRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomAreaResponse createRoomArea(@Valid RoomAreaCreationRequest request) {
        if (roomAreaRepository.existsByAreaName(request.getAreaName())) {
            throw new AppException(ErrorCode.AREA_NAME_EXISTED);
        }
        RoomArea roomArea = roomAreaMapper.toRoomArea(request);
        roomArea.setIsActive(true);
        roomArea = roomAreaRepository.save(roomArea);
        return roomAreaMapper.toRoomAreaResponse(roomArea);
    }

    @Override
    public List<RoomAreaResponse> getRoomAreas() {
        return roomAreaRepository.findAll().stream()
                .map(roomArea -> roomAreaMapper.toRoomAreaResponse(roomArea))
                .toList();
    }

    @Override
    public List<RoomAreaResponse> getRoomAreas(boolean isActive) {
        return roomAreaRepository.findByIsActive(isActive).stream()
                .map(roomArea -> roomAreaMapper.toRoomAreaResponse(roomArea))
                .toList();
    }

    @Override
    public RoomAreaResponse getRoomAreaById(Long id) {
        return roomAreaMapper.toRoomAreaResponse(
                roomAreaRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.AREA_NOT_EXISTED))
        );
    }

    @Override
    public RoomAreaResponse updateRoomArea(Long id, @Valid RoomAreaUpdateRequest request) {

        RoomArea roomArea = roomAreaRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.AREA_NOT_EXISTED)
        );

        if(roomAreaRepository.existsByAreaNameAndIdIsNot(request.getAreaName(), id)) {
            throw new AppException(ErrorCode.AREA_NAME_EXISTED);
        }

        if(!request.getIsActive() && roomRepository.existsByArea_IdAndIsActive(id, true)) {
            throw new AppException(ErrorCode.EXISTING_ACTIVE_ROOM_OF_AREA);
        }

        roomAreaMapper.updateRoomArea(roomArea, request);
        roomArea = roomAreaRepository.save(roomArea);
        return roomAreaMapper.toRoomAreaResponse(roomArea);
    }


}
