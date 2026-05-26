package com.bty.karaoke.mememusicboxservice.constant;

import lombok.Getter;

@Getter
public enum DefaultSystemConfig {
    VAT_PERCENT("VAT_PERCENT", "10", SystemConfigDataType.INT, "VAT percent (unit: %)"),
    MINIMUM_MINUTES_BEFORE_RESERVATION("MINIMUM_MINUTES_BEFORE_RESERVATION", "60",  SystemConfigDataType.INT, "Minimum minutes before reservation"),
    EARLIEST_BOOKING_HOURS_IN_DAY("EARLIEST_BOOKING_HOURS_IN_DAY", "8", SystemConfigDataType.INT, "Earliest booking hours in DAY"),
    LATEST_BOOKING_HOURS_IN_DAY("LATEST_BOOKING_HOURS_IN_DAY", "23", SystemConfigDataType.INT, "Latest booking hours in DAY"),
    MIN_ADVANCE_BOOKING_MINUTES("MIN_ADVANCE_BOOKING_MINUTES", "5",  SystemConfigDataType.INT, "Minimum advance booking minutes"),
    MONEY_AMOUNT_TO_LOYALTY_POINT("MONEY_AMOUNT_TO_LOYALTY_POINT", "10000",  SystemConfigDataType.INT, "Money amount to loyalty point (unit: VND)"),
    ;

    DefaultSystemConfig(String configKey, String configValue, SystemConfigDataType dataType, String description) {
        this.configKey = configKey;
        this.configValue = configValue;
        this.dataType = dataType;
        this.description = description;
    }

    private final String configKey;
    private final String configValue;
    private final SystemConfigDataType dataType;
    private final String description;
}
