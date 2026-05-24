package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Account;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.InvoiceMapper;
import com.bty.karaoke.mememusicboxservice.repository.AccountRepository;
import com.bty.karaoke.mememusicboxservice.repository.InvoiceRepository;
import com.bty.karaoke.mememusicboxservice.service.AccountService;
import com.bty.karaoke.mememusicboxservice.service.InvoiceService;
import com.bty.karaoke.mememusicboxservice.service.SystemConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
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
