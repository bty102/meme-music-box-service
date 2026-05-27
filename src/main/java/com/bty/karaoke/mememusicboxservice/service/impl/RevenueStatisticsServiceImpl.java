package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import com.bty.karaoke.mememusicboxservice.dto.response.AnnualRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.MonthlyRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.repository.InvoiceRepository;
import com.bty.karaoke.mememusicboxservice.service.RevenueStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RevenueStatisticsServiceImpl implements RevenueStatisticsService {

    private final InvoiceRepository invoiceRepository;

    @Override
    public List<MonthlyRevenueStatisticsResponse> getStatistics(
            int year,
            int month
    ) {

        List<MonthlyRevenueStatisticsResponse> data =
                invoiceRepository.getMonthlyRevenueStatistics(
                        year,
                        month,
                        InvoiceStatus.PAID
                );

        Map<Integer, BigDecimal> revenueMap = data.stream()
                .collect(Collectors.toMap(
                        MonthlyRevenueStatisticsResponse::getDay,
                        MonthlyRevenueStatisticsResponse::getRevenue
                ));

        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        List<MonthlyRevenueStatisticsResponse> result = new ArrayList<>();

        for (int day = 1; day <= daysInMonth; day++) {

            result.add(
                    MonthlyRevenueStatisticsResponse.builder()
                            .day(day)
                            .revenue(
                                    revenueMap.getOrDefault(
                                            day,
                                            BigDecimal.ZERO
                                    )
                            )
                            .build()
            );
        }

        return result;
    }

    @Override
    public List<AnnualRevenueStatisticsResponse> getAnnualStatistics(
            int year
    ) {

        List<AnnualRevenueStatisticsResponse> data =
                invoiceRepository.getAnnualRevenueStatistics(
                        year,
                        InvoiceStatus.PAID
                );

        Map<Integer, BigDecimal> revenueMap = data.stream()
                .collect(Collectors.toMap(
                        AnnualRevenueStatisticsResponse::getMonth,
                        AnnualRevenueStatisticsResponse::getRevenue
                ));

        List<AnnualRevenueStatisticsResponse> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            result.add(
                    AnnualRevenueStatisticsResponse.builder()
                            .month(month)
                            .revenue(
                                    revenueMap.getOrDefault(
                                            month,
                                            BigDecimal.ZERO
                                    )
                            )
                            .build()
            );
        }

        return result;
    }
}
