package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfInvoiceCreationRequest {

    @NotNull(message = "INVOICE_NOT_EXISTED")
    private Long invoiceId;

    @NotNull(message = "PRODUCT_NOT_EXISTED")
    private Long productId;

    @NotNull(message = "PRODUCT_OF_INVOICE_QUANTITY_NULL")
    @Min(value = 0, message = "PRODUCT_OF_INVOICE_QUANTITY_IN_INVALID_RANGE")
    private Integer quantity;
}
