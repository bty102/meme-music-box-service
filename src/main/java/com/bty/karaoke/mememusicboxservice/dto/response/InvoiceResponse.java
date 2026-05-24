package com.bty.karaoke.mememusicboxservice.dto.response;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponse {

    private Long id;

    private String invoiceCode;

    private BigDecimal roomCharge;

    private BigDecimal serviceCharge;

    private BigDecimal discountPercent;

    private BigDecimal discountAmount;

    private BigDecimal vatPercent;

    private BigDecimal vatAmount;

    private BigDecimal finalAmount;

    private InvoiceStatus status;

    private LocalDateTime createdAt;

    private AccountResponse createdBy;

    private AccountResponse member;

}
