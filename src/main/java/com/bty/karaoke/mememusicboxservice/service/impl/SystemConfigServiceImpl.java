package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.constant.DefaultSystemConfig;
import com.bty.karaoke.mememusicboxservice.entity.SystemConfig;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.repository.SystemConfigRepository;
import com.bty.karaoke.mememusicboxservice.service.SystemConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
