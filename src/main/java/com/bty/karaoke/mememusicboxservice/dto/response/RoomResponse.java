package com.bty.karaoke.mememusicboxservice.dto.response;

import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomResponse {

    private Long id;

    private Integer roomNumber;

    private Integer capacity;

    private BigDecimal hourlyRate;

    private RoomStatus status;

    private Boolean isActive;

    private RoomAreaResponse area;
}
