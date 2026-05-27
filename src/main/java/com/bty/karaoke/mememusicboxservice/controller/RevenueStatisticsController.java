package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.response.AnnualRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.MonthlyRevenueStatisticsResponse;
import com.bty.karaoke.mememusicboxservice.service.RevenueStatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class RevenueStatisticsController {

    private final RevenueStatisticsService revenueStatisticsService;

    @GetMapping(path = "/monthly", produces = "application/json")
    public ResponseEntity<ApiResponse<List<MonthlyRevenueStatisticsResponse>>> getMonthlyRevenueStatistics(
            @RequestParam(name = "month", required = true) int month,
            @RequestParam(name = "year", required = true) int year
    ) {
        var response = revenueStatisticsService.getStatistics(year, month);
        return ResponseEntity.ok(
                ApiResponse.<List<MonthlyRevenueStatisticsResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/annual", produces = "application/json")
    public ResponseEntity<ApiResponse<List<AnnualRevenueStatisticsResponse>>> getAnnualStatistics(
            @RequestParam(name = "year", required = true) int year
    ) {
        var response = revenueStatisticsService.getAnnualStatistics(year);
        return ResponseEntity.ok(
                ApiResponse.<List<AnnualRevenueStatisticsResponse>>builder()
                        .result(response)
                        .build()
        );
    }
}
