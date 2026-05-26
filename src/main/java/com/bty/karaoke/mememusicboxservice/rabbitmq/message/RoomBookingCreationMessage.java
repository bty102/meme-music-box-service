package com.bty.karaoke.mememusicboxservice.rabbitmq.message;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBookingCreationMessage {

    private RoomBookingCreationRequest request;
    private Long memberAccountId;
}
