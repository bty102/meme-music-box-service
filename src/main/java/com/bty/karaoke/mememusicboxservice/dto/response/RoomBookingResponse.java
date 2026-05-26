package com.bty.karaoke.mememusicboxservice.dto.response;

import com.bty.karaoke.mememusicboxservice.constant.RoomBookingStatus;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBookingResponse {

    private Long id;

    private LocalDateTime bookingTime;

    private RoomBookingStatus status;

    private LocalDateTime createdAt;

    private RoomResponse room;

    private AccountResponse memberAccount;
}
