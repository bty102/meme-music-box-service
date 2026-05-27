package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnnualRevenueStatisticsResponse {

    private Integer month;
    private BigDecimal revenue;
}
