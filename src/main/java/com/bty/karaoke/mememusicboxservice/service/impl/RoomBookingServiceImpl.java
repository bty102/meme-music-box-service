package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.RoomBookingStatus;
import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
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
import com.bty.karaoke.mememusicboxservice.service.InvoiceService;
import com.bty.karaoke.mememusicboxservice.service.RoomBookingService;
import com.bty.karaoke.mememusicboxservice.service.RoomOfInvoiceService;
import com.bty.karaoke.mememusicboxservice.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final RoomOfInvoiceService roomOfInvoiceService;
    private final InvoiceService invoiceService;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomBookingResponse checkIn(Long roomBookingId, Long creatorAccountId) {
        if(roomBookingId == null) {
            throw new AppException(ErrorCode.ROOM_BOOKING_NOT_EXISTED);
        }

        RoomBooking roomBooking = roomBookingRepository.findById(roomBookingId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_BOOKING_NOT_EXISTED));

        if(!roomBooking.getStatus().equals(RoomBookingStatus.PENDING)) {
            throw new AppException(ErrorCode.ROOM_BOOKING_STATUS_INVALID_TO_CHECK_IN);
        }

        if(roomBooking.getBookingTime().isAfter(LocalDateTime.now())) {
            throw new AppException(ErrorCode.CURRENT_TIME_CANNOT_CHECK_IN_ROOM_BOOKING);
        }

        Room room = roomBooking.getRoom();
        if(room.getStatus().equals(RoomStatus.IN_USE) || room.getStatus().equals(RoomStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.ROOM_STATUS_INVALID_TO_CHECK_IN_ROOM_BOOKING);
        }


        // Nhan phong thoi
        roomBooking.setStatus(RoomBookingStatus.CHECKEDIN);
        roomBooking = roomBookingRepository.save(roomBooking);

        InvoiceResponse invoiceResponse = invoiceService.createInvoice(
                InvoiceCreationRequest.builder()
                        .creatorAccountId(creatorAccountId)
                        .memberAccountId(roomBooking.getMemberAccount().getId())
                        .build()
        );

        roomOfInvoiceService.createRoomOfInvoice(
                RoomOfInvoiceCreationRequest.builder()
                        .roomId(room.getId())
                        .invoiceId(invoiceResponse.getId())
                        .build()
        );

        room = roomRepository.findById(room.getId()).get();
        if(room.getStatus().equals(RoomStatus.TEMPORARY)) {
            if(!roomBookingRepository.existsByRoom_IdAndStatus(room.getId(), RoomBookingStatus.PENDING)) {
                room.setStatus(RoomStatus.IN_USE);
            }
        }
        roomRepository.save(room);

        return roomBookingMapper.toRoomBookingResponse(roomBooking);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoomBookingResponse cancelRoomBooking(Long roomBookingId) {
        if(roomBookingId == null) {
            throw new AppException(ErrorCode.ROOM_BOOKING_NOT_EXISTED);
        }

        RoomBooking roomBooking = roomBookingRepository.findById(roomBookingId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_BOOKING_NOT_EXISTED));

        if(!roomBooking.getStatus().equals(RoomBookingStatus.PENDING)) {
            throw new AppException(ErrorCode.ROOM_BOOKING_STATUS_INVALID_TO_CANCEL);
        }

        roomBooking.setStatus(RoomBookingStatus.CANCELLED);
        roomBooking = roomBookingRepository.save(roomBooking);

        Room room = roomBooking.getRoom();
        if(room.getStatus().equals(RoomStatus.BOOKED)) {
            if(!roomBookingRepository.existsByRoom_IdAndStatus(room.getId(), RoomBookingStatus.PENDING)) {
                room.setStatus(RoomStatus.AVAILABLE);
            }
        }
        roomRepository.save(room);
        return roomBookingMapper.toRoomBookingResponse(roomBooking);
    }

    @Override
    public Page<RoomBookingResponse> getRoomBookingsOfRoom(Long roomId, int pageNumber, int pageSize) {

        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<RoomBooking> roomBookingPage = roomBookingRepository.findByRoom_IdOrderByCreatedAtDesc(roomId, pageable);
        return roomBookingPage.map(roomBookingMapper::toRoomBookingResponse);
    }

    @Override
    public Page<RoomBookingResponse> getRoomBookingsOfMember(Long memberAccountId, int pageNumber, int pageSize) {

        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<RoomBooking> roomBookingPage = roomBookingRepository.findByMemberAccount_IdOrderByCreatedAtDesc(memberAccountId, pageable);
        return roomBookingPage.map(roomBookingMapper::toRoomBookingResponse);
    }

    @Override
    public RoomBookingResponse getRoomBookingById(Long roomBookingId) {
        if(roomBookingId == null) {
            throw new AppException(ErrorCode.ROOM_BOOKING_NOT_EXISTED);
        }
        RoomBooking roomBooking = roomBookingRepository.findById(roomBookingId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_BOOKING_NOT_EXISTED));
        return roomBookingMapper.toRoomBookingResponse(roomBooking);
    }
}
