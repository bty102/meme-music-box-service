package com.bty.karaoke.mememusicboxservice.dto.request;

import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUpdateRequest {

    @NotNull(message = "PRODUCT_CODE_NULL")
    @Size(min = 1, max = 20, message = "PRODUCT_CODE_SIZE_NOT_VALID")
    private String productCode;

    @NotNull(message = "PRODUCT_NAME_NULL")
    @Size(min = 1, max = 100, message = "PRODUCT_NAME_SIZE_NOT_VALID")
    private String productName;

    @NotNull(message = "PRODUCT_UNIT_NULL")
    @Size(min = 1, max = 20, message = "PRODUCT_UNIT_SIZE_NOT_VALID")
    private String unit;

    @NotNull(message = "PRODUCT_UNIT_PRICE_NULL")
    @Min(value = 0, message = "PRODUCT_UNIT_PRICE_RANGE_NOT_VALID")
    private BigDecimal unitPrice;

    @NotNull(message = "PRODUCT_STOCK_QUANTITY_NULL")
    @Min(value = 0, message = "PRODUCT_STOCK_QUANTITY_RANGE_NOT_VALID")
    private Integer stockQuantity;

    @NotNull(message = "PRODUCT_ACTIVE_STATE_NULL")
    private Boolean isActive;
}
