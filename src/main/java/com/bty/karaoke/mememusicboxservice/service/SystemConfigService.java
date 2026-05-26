package com.bty.karaoke.mememusicboxservice.service;

import java.time.LocalDateTime;

public interface SystemConfigService {

    /**
     *
     * @return VAT percent (unit: %)
     */
    public int getVATPercent();

    public int getMinimumMinutesBeforeReservation();

    public int getMinimumAdvanceBookingMinutes();

    public LocalDateTime getEarliestBookingTime();

    public LocalDateTime getLatestBookingTime();

    public int getMoneyAmountVNDToLoyaltyPoint();
}
