package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.DefaultSystemConfig;
import com.bty.karaoke.mememusicboxservice.entity.SystemConfig;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.repository.SystemConfigRepository;
import com.bty.karaoke.mememusicboxservice.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SystemConfigServiceImpl implements SystemConfigService {

    private final SystemConfigRepository systemConfigRepository;

    @Override
    public int getVATPercent() {
        SystemConfig vatPercentSystemConfig = systemConfigRepository.findByConfigKey(DefaultSystemConfig.VAT_PERCENT.getConfigKey())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_SYSTEM_CONFIG_NOT_CREATED));

        int VATPercent = Integer.parseInt(vatPercentSystemConfig.getConfigValue());
        return VATPercent;
    }

    @Override
    public int getMinimumMinutesBeforeReservation() {
        SystemConfig minimumMinutesBeforeReservationSystemConfig = systemConfigRepository.findByConfigKey(DefaultSystemConfig.MINIMUM_MINUTES_BEFORE_RESERVATION.getConfigKey())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_SYSTEM_CONFIG_NOT_CREATED));

        int minimumMinutesBeforeReservation = Integer.parseInt(minimumMinutesBeforeReservationSystemConfig.getConfigValue());
        return minimumMinutesBeforeReservation;
    }

    @Override
    public int getMinimumAdvanceBookingMinutes() {
        SystemConfig minimumAdvanceBookingMinutesSystemConfig = systemConfigRepository.findByConfigKey(DefaultSystemConfig.MIN_ADVANCE_BOOKING_MINUTES.getConfigKey())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_SYSTEM_CONFIG_NOT_CREATED));

        int minimumAdvanceBookingMinutes = Integer.parseInt(minimumAdvanceBookingMinutesSystemConfig.getConfigValue());
        return minimumAdvanceBookingMinutes;
    }

    @Override
    public LocalDateTime getEarliestBookingTime() {
        SystemConfig earliestBookingHoursInDaySystemConfig = systemConfigRepository.findByConfigKey(DefaultSystemConfig.EARLIEST_BOOKING_HOURS_IN_DAY.getConfigKey())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_SYSTEM_CONFIG_NOT_CREATED));

        int earliestBookingHoursInDay = Integer.parseInt(earliestBookingHoursInDaySystemConfig.getConfigValue());
        return LocalDate.now()
                .atTime(earliestBookingHoursInDay, 0);
    }

    @Override
    public LocalDateTime getLatestBookingTime() {
        SystemConfig latestBookingHoursInDaySystemConfig = systemConfigRepository.findByConfigKey(DefaultSystemConfig.LATEST_BOOKING_HOURS_IN_DAY.getConfigKey())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_SYSTEM_CONFIG_NOT_CREATED));

        int latestBookingHoursInDay = Integer.parseInt(latestBookingHoursInDaySystemConfig.getConfigValue());
        return LocalDate.now()
                .atTime(latestBookingHoursInDay, 0);
    }

    @Override
    public int getMoneyAmountVNDToLoyaltyPoint() {
        SystemConfig systemConfig = systemConfigRepository.findByConfigKey(DefaultSystemConfig.MONEY_AMOUNT_TO_LOYALTY_POINT.getConfigKey())
                .orElseThrow(() -> new AppException(ErrorCode.DEFAULT_SYSTEM_CONFIG_NOT_CREATED));
        int moneyAmountVNDToLoyaltyPoint = Integer.parseInt(systemConfig.getConfigValue());
        return moneyAmountVNDToLoyaltyPoint;
    }

}
