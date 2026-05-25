package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import com.bty.karaoke.mememusicboxservice.constant.Role;
import com.bty.karaoke.mememusicboxservice.constant.RoomBookingStatus;
import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.*;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.InvoiceMapper;
import com.bty.karaoke.mememusicboxservice.repository.*;
import com.bty.karaoke.mememusicboxservice.service.AccountService;
import com.bty.karaoke.mememusicboxservice.service.InvoiceService;
import com.bty.karaoke.mememusicboxservice.service.RoomOfInvoiceService;
import com.bty.karaoke.mememusicboxservice.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final AccountRepository accountRepository;
    private final InvoiceMapper invoiceMapper;
    private final SystemConfigService systemConfigService;
    private final AccountService accountService;
    private final RoomRepository roomRepository;
    private final RoomBookingRepository roomBookingRepository;
    private final RoomOfInvoiceRepository roomOfInvoiceRepository;
    private final RoomOfInvoiceService roomOfInvoiceService;

    @Override
    public InvoiceResponse createInvoice(@Valid InvoiceCreationRequest request) {
        Account creator = accountRepository.findById(request.getCreatorAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        if (!creator.getIsActive()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }

        Account member = null;
        if (request.getMemberAccountId() != null) {
            member = accountRepository.findById(request.getMemberAccountId())
                    .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

            if (!member.getIsActive()) {
                throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
            }
        }

        BigDecimal discountPercent = BigDecimal.ZERO;
        if(member != null) {
            discountPercent = accountService.getDiscountPercentByMemberAccountId(member.getId());
        }

        Invoice invoice = invoiceMapper.toInvoice(request);
        invoice.setInvoiceCode(generateInvoiceCode());
        invoice.setCreatedByAccount(creator);
        invoice.setMemberAccount(member);
        invoice.setVatPercent(BigDecimal.valueOf(systemConfigService.getVATPercent()));
        invoice.setDiscountPercent(discountPercent);
        invoice.setStatus(InvoiceStatus.TEMPORARY);
        invoice.setCreatedAt(LocalDateTime.now());
        invoice.setRoomCharge(null);
        invoice.setServiceCharge(null);
        invoice.setDiscountAmount(null);
        invoice.setVatAmount(null);
        invoice.setFinalAmount(null);
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    @Override
    public InvoiceResponse updateMemberOfInvoice(Long invoiceId, Long memberAccountId) {
        if(invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if(!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_UPDATE);
        }

        if(memberAccountId == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
        Account account = accountRepository.findById(memberAccountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        if(!account.getIsActive()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }
        if(!account.getRole().equals(Role.MEMBER)) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_MEMBER);
        }

        invoice.setMemberAccount(account);
        invoice.setDiscountPercent(accountService.getDiscountPercentByMemberAccountId(memberAccountId));
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(invoice);

    }

    @Override
    public InvoiceResponse deleteMemberOfInvoice(Long invoiceId) {

        if(invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if(!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_UPDATE);
        }

        invoice.setMemberAccount(null);
        invoice.setDiscountPercent(BigDecimal.ZERO);
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void transferRoomOfInvoice(Long invoiceId, Long transferToRoomId) {
        if(invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if(!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_TRANSFER_TO_ROOM);
        }

        if(transferToRoomId == null) {
            throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
        }

        Room room = roomRepository.findById(transferToRoomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        if(!room.getIsActive()) {
            throw new AppException(ErrorCode.ROOM_NOT_ACTIVE);
        }

        if(room.getStatus().equals(RoomStatus.IN_USE) || room.getStatus().equals(RoomStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.ROOM_STATUS_INVALID_TO_TRANSFER_TO);
        }


        if(room.getStatus().equals(RoomStatus.BOOKED)) {
            RoomBooking roomBooking = roomBookingRepository.findFirstByRoomIdAndStatusOrderByBookingTimeAsc(transferToRoomId, RoomBookingStatus.PENDING)
                    .get();

            if(!roomBooking.getBookingTime().isAfter(LocalDateTime.now())) {
                throw new AppException(ErrorCode.CURRENT_TIME_INVALID_TO_TRANSFER_TO_ROOM);
            }

            int minimumMinutesBeforeReservation = systemConfigService.getMinimumMinutesBeforeReservation();

            if(Duration.between(LocalDateTime.now(), roomBooking.getBookingTime()).toMinutes()
                    <= minimumMinutesBeforeReservation
            ) {
                throw new AppException(ErrorCode.CURRENT_TIME_INVALID_TO_TRANSFER_TO_ROOM);
            }
        }

        if(roomOfInvoiceRepository.existsByInvoice_IdAndRoom_IdAndIsTransferred(invoiceId, transferToRoomId, false)) {
            throw new AppException(ErrorCode.CURRENT_ROOM_ON_BILL);
        }

        // Chuyen thoi
        RoomOfInvoice roomOfInvoice = roomOfInvoiceRepository.findByInvoice_IdAndIsTransferred(invoiceId, false)
                .get();

        Room currentRoom = roomOfInvoice.getRoom();

        if(room.getCapacity() < currentRoom.getCapacity()) {
            throw new AppException(ErrorCode.ROOM_CAPACITY_NOT_ENOUGH_TO_TRANSFER_TO);
        }

        roomOfInvoice.setCheckOutAt(LocalDateTime.now());
        long minutes = Duration.between(roomOfInvoice.getCheckInAt(), roomOfInvoice.getCheckOutAt()).toMinutes();
        roomOfInvoice.setDurationHours(BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
        roomOfInvoice.setRoomCharge(roomOfInvoice.getDurationHours().multiply(roomOfInvoice.getRoom().getHourlyRate())
                .setScale(0, RoundingMode.HALF_UP));
        roomOfInvoice.setIsTransferred(true);
        roomOfInvoiceRepository.save(roomOfInvoice);

        if(currentRoom.getStatus().equals(RoomStatus.IN_USE)) {
            currentRoom.setStatus(RoomStatus.AVAILABLE);
        }
        if(currentRoom.getStatus().equals(RoomStatus.TEMPORARY)) {
            currentRoom.setStatus(RoomStatus.BOOKED);
        }
        roomRepository.save(currentRoom);

        RoomOfInvoiceResponse roomOfInvoiceResponse = roomOfInvoiceService.createRoomOfInvoice(
                RoomOfInvoiceCreationRequest.builder()
                        .roomId(transferToRoomId)
                        .invoiceId(invoiceId)
                        .build()
        );


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkOut(Long invoiceId) {
        if(invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if(!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_CHECK_OUT);
        }

        // Check out thoi
        RoomOfInvoice roomOfInvoice = roomOfInvoiceRepository.findByInvoice_IdAndIsTransferred(invoiceId, false)
                .get();

        Room currentRoom = roomOfInvoice.getRoom();


        roomOfInvoice.setCheckOutAt(LocalDateTime.now());
        long minutes = Duration.between(roomOfInvoice.getCheckInAt(), roomOfInvoice.getCheckOutAt()).toMinutes();
        roomOfInvoice.setDurationHours(BigDecimal.valueOf(minutes)
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP));
        roomOfInvoice.setRoomCharge(roomOfInvoice.getDurationHours().multiply(roomOfInvoice.getRoom().getHourlyRate())
                .setScale(0, RoundingMode.HALF_UP));
        roomOfInvoiceRepository.save(roomOfInvoice);

        if(currentRoom.getStatus().equals(RoomStatus.IN_USE)) {
            currentRoom.setStatus(RoomStatus.AVAILABLE);
        }
        if(currentRoom.getStatus().equals(RoomStatus.TEMPORARY)) {
            currentRoom.setStatus(RoomStatus.BOOKED);
        }
        roomRepository.save(currentRoom);


        invoice.calculateAmounts();
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoiceRepository.save(invoice);

    }

    private String generateInvoiceCode() {

        String datePart = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String randomPart = UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 6)
                .toUpperCase();

        return "INV" + datePart + randomPart;

    }
}
