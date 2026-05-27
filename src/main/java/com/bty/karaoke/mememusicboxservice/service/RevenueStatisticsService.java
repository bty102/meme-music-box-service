package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.response.AnnualRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.MonthlyRevenueStatisticsResponse;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RevenueStatisticsService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public List<MonthlyRevenueStatisticsResponse> getStatistics(
            int year,
            int month
    );

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public List<AnnualRevenueStatisticsResponse> getAnnualStatistics(
            int year
    );
}
