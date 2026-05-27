package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyRevenueStatisticsResponse {

    private Integer day;
    private BigDecimal revenue;
}
