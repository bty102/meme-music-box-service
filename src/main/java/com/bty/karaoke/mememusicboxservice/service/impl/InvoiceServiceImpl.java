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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final MemberProfileRepository memberProfileRepository;

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
        if (member != null) {
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
        if (invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_UPDATE);
        }

        if (memberAccountId == null) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }
        Account account = accountRepository.findById(memberAccountId)
                .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));
        if (!account.getIsActive()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_ACTIVE);
        }
        if (!account.getRole().equals(Role.MEMBER)) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_MEMBER);
        }

        invoice.setMemberAccount(account);
        invoice.setDiscountPercent(accountService.getDiscountPercentByMemberAccountId(memberAccountId));
        invoice = invoiceRepository.save(invoice);
        return invoiceMapper.toInvoiceResponse(invoice);

    }

    @Override
    public InvoiceResponse deleteMemberOfInvoice(Long invoiceId) {

        if (invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
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
        if (invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_TRANSFER_TO_ROOM);
        }

        if (transferToRoomId == null) {
            throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
        }

        Room room = roomRepository.findById(transferToRoomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        if (!room.getIsActive()) {
            throw new AppException(ErrorCode.ROOM_NOT_ACTIVE);
        }

        if (room.getStatus().equals(RoomStatus.IN_USE) || room.getStatus().equals(RoomStatus.TEMPORARY)) {
            throw new AppException(ErrorCode.ROOM_STATUS_INVALID_TO_TRANSFER_TO);
        }


        if (room.getStatus().equals(RoomStatus.BOOKED)) {
            RoomBooking roomBooking = roomBookingRepository.findFirstByRoomIdAndStatusOrderByBookingTimeAsc(transferToRoomId, RoomBookingStatus.PENDING)
                    .get();

            if (!roomBooking.getBookingTime().isAfter(LocalDateTime.now())) {
                throw new AppException(ErrorCode.CURRENT_TIME_INVALID_TO_TRANSFER_TO_ROOM);
            }

            int minimumMinutesBeforeReservation = systemConfigService.getMinimumMinutesBeforeReservation();

            if (Duration.between(LocalDateTime.now(), roomBooking.getBookingTime()).toMinutes()
                    <= minimumMinutesBeforeReservation
            ) {
                throw new AppException(ErrorCode.CURRENT_TIME_INVALID_TO_TRANSFER_TO_ROOM);
            }
        }

        if (roomOfInvoiceRepository.existsByInvoice_IdAndRoom_IdAndIsTransferred(invoiceId, transferToRoomId, false)) {
            throw new AppException(ErrorCode.CURRENT_ROOM_ON_BILL);
        }

        // Chuyen thoi
        RoomOfInvoice roomOfInvoice = roomOfInvoiceRepository.findByInvoice_IdAndIsTransferred(invoiceId, false)
                .get();

        Room currentRoom = roomOfInvoice.getRoom();

        if (room.getCapacity() < currentRoom.getCapacity()) {
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

        if (currentRoom.getStatus().equals(RoomStatus.IN_USE)) {
            currentRoom.setStatus(RoomStatus.AVAILABLE);
        }
        if (currentRoom.getStatus().equals(RoomStatus.TEMPORARY)) {
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
        if (invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (!invoice.getStatus().equals(InvoiceStatus.TEMPORARY)) {
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

        if (currentRoom.getStatus().equals(RoomStatus.IN_USE)) {
            currentRoom.setStatus(RoomStatus.AVAILABLE);
        }
        if (currentRoom.getStatus().equals(RoomStatus.TEMPORARY)) {
            currentRoom.setStatus(RoomStatus.BOOKED);
        }
        roomRepository.save(currentRoom);


        invoice.calculateAmounts();
        invoice.setStatus(InvoiceStatus.UNPAID);
        invoiceRepository.save(invoice);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void paymentConfirmation(Long invoiceId) {
        if (invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        if (!invoice.getStatus().equals(InvoiceStatus.UNPAID)) {
            throw new AppException(ErrorCode.INVOICE_STATUS_INVALID_TO_CONFIRM_PAYMENT);
        }

        invoice.setStatus(InvoiceStatus.PAID);
        invoice = invoiceRepository.save(invoice);

        if (invoice.getMemberAccount() != null) {
            // Diem thuong cong them
            int additionalLoyaltyPoint = invoice.getFinalAmount().divide(BigDecimal.valueOf(systemConfigService.getMoneyAmountVNDToLoyaltyPoint())).intValue();
            Account member = invoice.getMemberAccount();
            MemberProfile memberProfile = member.getMemberProfile();
            memberProfile.setLoyaltyPoint(memberProfile.getLoyaltyPoint() + additionalLoyaltyPoint);
            memberProfileRepository.save(memberProfile);
        }
    }

    @Override
    public InvoiceResponse getTemporaryInvoiceOfRoom(Long roomId) {
        if (roomId == null) {
            throw new AppException(ErrorCode.ROOM_NOT_EXISTED);
        }

        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new AppException(ErrorCode.ROOM_NOT_EXISTED));

        RoomOfInvoice roomOfInvoice = roomOfInvoiceRepository
                .findByRoom_IdAndInvoice_StatusAndIsTransferred(roomId, InvoiceStatus.TEMPORARY, false)
                .orElseThrow(() -> new AppException(ErrorCode.TEMPORARY_INVOICE_OF_ROOM_NOT_FOUND));

        Invoice invoice = roomOfInvoice.getInvoice();
        return invoiceMapper.toInvoiceResponse(invoice);
    }

    @Override
    public Page<InvoiceResponse> getInvoicesOfRoom(Long roomId, int pageNumber, int pageSize) {
        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Invoice> invoices =
                invoiceRepository.findDistinctByRoomOfInvoices_Room_IdAndRoomOfInvoices_IsTransferredOrderByCreatedAtDesc(roomId, false, pageable);
        return invoices.map(invoiceMapper::toInvoiceResponse);
    }

    @Override
    public Page<InvoiceResponse> getInvoices(int pageNumber, int pageSize) {

        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Invoice> invoicePage = invoiceRepository.findAllByOrderByCreatedAtDesc(pageable);
        return invoicePage.map(invoiceMapper::toInvoiceResponse);
    }

    @Override
    public Page<InvoiceResponse> getInvoicesByCreatorAccId(Long creatorAccountId, int pageNumber, int pageSize) {

        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Invoice> invoicePage = invoiceRepository.findByCreatedByAccount_IdOrderByCreatedAtDesc(creatorAccountId, pageable);
        return invoicePage.map(invoiceMapper::toInvoiceResponse);
    }

    @Override
    public Page<InvoiceResponse> getInvoicesOfMemberAccId(Long memberAccountId, int pageNumber, int pageSize) {

        if (pageNumber < 0) {
            pageNumber = 0;
        }
        if (pageSize < 1) {
            pageSize = 1;
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Invoice> invoicePage = invoiceRepository.findByMemberAccount_IdOrderByCreatedAtDesc(memberAccountId, pageable);
        return invoicePage.map(invoiceMapper::toInvoiceResponse);
    }

    @Override
    public InvoiceResponse getInvoice(Long invoiceId) {
        if(invoiceId == null) {
            throw new AppException(ErrorCode.INVOICE_NOT_EXISTED);
        }
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.INVOICE_NOT_EXISTED));

        return invoiceMapper.toInvoiceResponse(invoice);
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
