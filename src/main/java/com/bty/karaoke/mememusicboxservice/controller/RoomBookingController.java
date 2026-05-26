package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomBookingCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomBookingResponse;
import com.bty.karaoke.mememusicboxservice.rabbitmq.message.RoomBookingCreationMessage;
import com.bty.karaoke.mememusicboxservice.service.RoomBookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/booking")
@RequiredArgsConstructor
public class RoomBookingController {

    private final RabbitTemplate rabbitTemplate;
    private final RoomBookingService roomBookingService;

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

    @GetMapping(path = "/checkIn", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> checkIn(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "roomBookingId", required = true) Long roomBookingId
    ){

        Long creatorAccountId = Long.parseLong(jwt.getClaims().get("accId").toString());
        var response = roomBookingService.checkIn(roomBookingId, creatorAccountId);
        return ResponseEntity.ok(
                ApiResponse.<RoomBookingResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/cancel", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> cancelRoomBooking(
       @RequestParam(name = "roomBookingId", required = true) Long roomBookingId
    ) {
        var response = roomBookingService.cancelRoomBooking(roomBookingId);
        return ResponseEntity.ok(
                ApiResponse.<RoomBookingResponse>builder()
                        .result(response)
                        .build()
        );
    }
}
