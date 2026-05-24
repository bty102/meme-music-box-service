package com.bty.karaoke.mememusicboxservice.service;

public interface SystemConfigService {

    /**
     *
     * @return VAT percent (unit: %)
     */
    public int getVATPercent();

    public int getMinimumMinutesBeforeReservation();
}
