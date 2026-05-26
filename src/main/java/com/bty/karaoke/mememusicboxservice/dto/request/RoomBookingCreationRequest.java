package com.bty.karaoke.mememusicboxservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBookingCreationRequest {

//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "BOOKING_TIME_NULL")
    private LocalDateTime bookingTime;

    @NotNull(message = "ROOM_NOT_EXISTED")
    private Long roomId;

}
