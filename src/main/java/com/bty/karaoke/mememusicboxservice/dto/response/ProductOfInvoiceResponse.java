package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfInvoiceResponse {

    private Long id;

    private InvoiceResponse invoice;

    private ProductResponse product;

    private Integer quantity;

    private BigDecimal lineTotal;
}
