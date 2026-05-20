package com.bty.karaoke.mememusicboxservice.dto.request;

import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomCreationRequest {

    @NotNull(message = "ROOM_NUMBER_NULL")
    @Min(value = 1, message = "ROOM_NUMBER_RANGE_NOT_VALID")
    private Integer roomNumber;

    @NotNull(message = "ROOM_CAPACITY_NULL")
    @Min(value = 1, message = "ROOM_CAPACITY_RANGE_NOT_VALID")
    private Integer capacity;

    @NotNull(message = "ROOM_HOURLY_RATE_NULL")
    @Min(value = 0, message = "ROOM_HOURLY_RATE_RANGE_NOT_VALID")
    private BigDecimal hourlyRate;

    @NotNull(message = "AREA_ID_NULL")
    private Long areaId;
}
