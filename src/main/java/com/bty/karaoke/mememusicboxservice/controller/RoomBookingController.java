package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.rabbitmq.message.RoomBookingCreationMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/booking")
@RequiredArgsConstructor
public class RoomBookingController {

    private final RabbitTemplate rabbitTemplate;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> createRoomBooking(
            @Valid @RequestBody RoomBookingCreationRequest request,
            @AuthenticationPrincipal Jwt jwt
            ) {
        Long memberAccountId = Long.parseLong(jwt.getClaims().get("accId").toString());

        RoomBookingCreationMessage message = RoomBookingCreationMessage.builder()
                .request(request)
                .memberAccountId(memberAccountId)
                .build();

        rabbitTemplate.convertAndSend("meme.exchange", "booking.queue", message);

        return ResponseEntity.accepted()
                .body(ApiResponse.<Void>builder().build());
    }
}
