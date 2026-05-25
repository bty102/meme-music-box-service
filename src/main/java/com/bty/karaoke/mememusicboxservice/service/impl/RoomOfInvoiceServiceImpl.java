package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import com.bty.karaoke.mememusicboxservice.entity.Room;
import com.bty.karaoke.mememusicboxservice.entity.RoomOfInvoice;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.RoomOfInvoiceMapper;
import com.bty.karaoke.mememusicboxservice.repository.InvoiceRepository;
import com.bty.karaoke.mememusicboxservice.repository.RoomOfInvoiceRepository;
import com.bty.karaoke.mememusicboxservice.repository.RoomRepository;
import com.bty.karaoke.mememusicboxservice.service.RoomOfInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class RoomOfInvoiceServiceImpl implements RoomOfInvoiceService {

    private final RoomOfInvoiceRepository roomOfInvoiceRepository;
    private final RoomRepository roomRepository;
    private final InvoiceRepository invoiceRepository;
    private final RoomOfInvoiceMapper roomOfInvoiceMapper;

    @Override
    @Transactional(rollbackFor = AppException.class)
    public RoomOfInvoiceResponse createRoomOfInvoice(@Valid RoomOfInvoiceCreationRequest request) {
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));
        if(!room.getIsActive()) {
            throw new AppException(ErrorCode.ROOM_NOT_ACTIVE);
        }

        if(room.getStatus().equals(RoomStatus.IN_USE) || room.getStatus().equals(RoomStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.ROOM_STATUS_NOT_VALID_TO_CREATE_ROOM_OF_INVOICE);
        }

        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

//        if(roomOfInvoiceRepository.existsByRoom_IdAndInvoice_Id(request.getRoomId(), request.getInvoiceId())) {
//            throw new AppException(ErrorCode.ROOM_OF_INVOICE_EXISTED);
//        }

        if(roomOfInvoiceRepository.existsByInvoice_IdAndIsTransferred(request.getInvoiceId(), false)) {
            throw new AppException(ErrorCode.EXISTING_ROOM_OF_INVOICE_NOT_TRANSFERRED);
        }

        RoomOfInvoice roomOfInvoice = roomOfInvoiceMapper.toRoomOfInvoice(request);
        roomOfInvoice.setRoom(room);
        roomOfInvoice.setInvoice(invoice);
        roomOfInvoice.setCheckInAt(LocalDateTime.now());
        roomOfInvoice.setCheckOutAt(null);
        roomOfInvoice.setDurationHours(null);
        roomOfInvoice.setRoomCharge(null);
        roomOfInvoice.setIsTransferred(false);
        roomOfInvoice = roomOfInvoiceRepository.save(roomOfInvoice);

        if(room.getStatus().equals(RoomStatus.AVAILABLE)) {
            room.setStatus(RoomStatus.IN_USE);
        }
        if(room.getStatus().equals(RoomStatus.BOOKED)) {
            room.setStatus(RoomStatus.TEMPORARY);
        }
        roomRepository.save(room);
        return roomOfInvoiceMapper.toRoomOfInvoiceResponse(roomOfInvoice);
    }
}
