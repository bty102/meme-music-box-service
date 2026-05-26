package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.RoomBookingStatus;
import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.Room;
import com.bty.karaoke.mememusicboxservice.entity.RoomBooking;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.RoomBookingMapper;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.RoomBookingRepository;
import com.bty.karaoke.mememusicboxservice.repository.RoomRepository;
import com.bty.karaoke.mememusicboxservice.service.RoomBookingService;
import com.bty.karaoke.mememusicboxservice.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class RoomBookingServiceImpl implements RoomBookingService {

    private final RoomBookingRepository roomBookingRepository;
    private final SystemConfigService systemConfigService;
    private final AccountRepository accountRepository;
    private final RoomRepository roomRepository;
    private final RoomBookingMapper roomBookingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomBookingResponse createRoomBooking(@Valid RoomBookingCreationRequest request, Long memberAccountId) {
        if (request.getBookingTime().isBefore(systemConfigService.getEarliestBookingTime())) {
            throw new AppException(ErrorCode.BOOKING_TIME_INVALID);
        }

        if (request.getBookingTime().isAfter(systemConfigService.getLatestBookingTime())) {
            throw new AppException(ErrorCode.BOOKING_TIME_INVALID);
        }

        if (request.getBookingTime().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.BOOKING_TIME_INVALID);
        }

        if (Duration.between(LocalDateTime.now(), request.getBookingTime()).toMinutes()
                < systemConfigService.getMinimumAdvanceBookingMinutes()) {
            throw new AppException(ErrorCode.BOOKING_TIME_INVALID);
        }

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        if (!room.getIsActive()) {
            throw new AppException(ErrorCode.ROOM_NOT_ACTIVE);
        }

        if (room.getStatus().equals(RoomStatus.IN_USE) || room.getStatus().equals(RoomStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.ROOM_STATUS_INVALID_TO_BOOK);
        }

        if (room.getStatus().equals(RoomStatus.BOOKED)) {
            LocalDateTime bookingTime = request.getBookingTime();

            LocalDateTime start =
                    bookingTime.minusMinutes(systemConfigService.getMinimumMinutesBeforeReservation());

            LocalDateTime end =
                    bookingTime.plusMinutes(systemConfigService.getMinimumMinutesBeforeReservation());
            if (roomBookingRepository.existsByRoom_IdAndBookingTimeBetween(request.getRoomId(), start, end)) {
                throw new AppException(ErrorCode.BOOKING_TIME_INVALID);
            }
        }

        if(memberAccountId == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
        Account member =  accountRepository.findById(memberAccountId)
                .orElseThrow(() ->  new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        if(!member.getIsActive()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }

        // Dat phong thoi
        RoomBooking roomBooking = roomBookingMapper.toRoomBooking(request);
        roomBooking.setRoom(room);
        roomBooking.setMemberAccount(member);
        roomBooking.setStatus(RoomBookingStatus.PENDING);
        roomBooking.setCreatedAt(LocalDateTime.now());
        roomBooking =  roomBookingRepository.save(roomBooking);

        if(room.getStatus().equals(RoomStatus.AVAILABLE)) {
            room.setStatus(RoomStatus.BOOKED);
        }

        roomRepository.save(room);

        return roomBookingMapper.toRoomBookingResponse(roomBooking);
    }
}
