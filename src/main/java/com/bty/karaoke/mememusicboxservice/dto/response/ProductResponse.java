package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;

    private String productCode;

    private String productName;

    private String unit;

    private BigDecimal unitPrice;

    private Integer stockQuantity;

    private String imageUrl;

    private Boolean isActive;

    private LocalDateTime createdAt;

}
