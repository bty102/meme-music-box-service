package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.Max;
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
public class PointDiscountCreationRequest {

    @NotNull(message = "POINT_DISCOUNT_REQUIRED_POINT_NULL")
    @Min(value = 0, message = "POINT_DISCOUNT_REQUIRED_POINT_IN_INVALID_RANGE")
    private Integer requiredPoint;

    @NotNull(message = "POINT_DISCOUNT_DISCOUNT_PERCENT_NULL")
    @Min(value = 0, message = "POINT_DISCOUNT_DISCOUNT_PERCENT_IN_INVALID_RANGE")
    @Max(value = 100, message = "POINT_DISCOUNT_DISCOUNT_PERCENT_IN_INVALID_RANGE")
    private BigDecimal discountPercent;

    @Size(min = 0, max = 255, message = "POINT_DISCOUNT_DESCRIPTION_SIZE_NOT_VALID")
    private String description;
}
