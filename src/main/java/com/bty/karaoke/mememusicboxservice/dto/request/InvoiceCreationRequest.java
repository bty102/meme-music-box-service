package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceCreationRequest {

//    @NotNull(message = "INVOICE_DISCOUNT_PERCENT_NULL")
//    @Min(value = 0, message = "INVOICE_DISCOUNT_PERCENT_IN_INVALID_RANGE")
//    @Max(value = 100, message = "INVOICE_DISCOUNT_PERCENT_IN_INVALID_RANGE")
//    private BigDecimal discountPercent;
//
//    @NotNull(message = "INVOICE_VAT_PERCENT_NULL")
//    @Min(value = 0, message = "INVOICE_VAT_PERCENT_IN_INVALID_RANGE")
//    @Max(value = 100, message = "INVOICE_VAT_PERCENT_IN_INVALID_RANGE")
//    private BigDecimal vatPercent;

    @NotNull(message = "INVOICE_CREATOR_NULL")
    private Long creatorAccountId;

    private Long memberAccountId;
}
