package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import com.bty.karaoke.mememusicboxservice.entity.RoomArea;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.RoomAreaMapper;
import com.bty.karaoke.mememusicboxservice.repository.RoomAreaRepository;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class RoomAreaServiceImpl implements RoomAreaService {

    private final RoomAreaRepository roomAreaRepository;
    private final RoomAreaMapper roomAreaMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomAreaResponse createRoomArea(@Valid RoomAreaCreationRequest request) {
        if(roomAreaRepository.existsByAreaName(request.getAreaName())) {
            throw new AppException(ErrorCode.AREA_NAME_EXISTED);
        }
        RoomArea roomArea = roomAreaMapper.toRoomArea(request);
        roomArea.setIsActive(true);
        roomArea = roomAreaRepository.save(roomArea);
        return roomAreaMapper.toRoomAreaResponse(roomArea);
    }
}
