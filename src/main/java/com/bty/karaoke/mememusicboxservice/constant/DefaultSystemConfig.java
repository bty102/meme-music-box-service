package com.bty.karaoke.mememusicboxservice.constant;

import lombok.Getter;

@Getter
public enum DefaultSystemConfig {
    VAT_PERCENT("VAT_PERCENT", "10", SystemConfigDataType.INT, "VAT percent (unit: %)"),
    MINIMUM_MINUTES_BEFORE_RESERVATION("MINIMUM_MINUTES_BEFORE_RESERVATION", "60",  SystemConfigDataType.INT, "Minimum minutes before reservation"),
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
