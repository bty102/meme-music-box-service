package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class RoomAreaController {

    private final RoomAreaService roomAreaService;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomAreaResponse>> createRoomArea(@Valid @RequestBody RoomAreaCreationRequest request){
        var response = roomAreaService.createRoomArea(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoomAreaResponse>builder().result(response).build());
    }
}
