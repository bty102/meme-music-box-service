package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDiscountResponse {

    private Long id;

    private Integer requiredPoint;

    private BigDecimal discountPercent;

    private String description;
}
