package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.RoomAreaUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomAreaResponse;
import com.bty.karaoke.mememusicboxservice.service.RoomAreaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/areas")
@RequiredArgsConstructor
public class RoomAreaController {

    private final RoomAreaService roomAreaService;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomAreaResponse>> createRoomArea(@Valid @RequestBody RoomAreaCreationRequest request) {
        var response = roomAreaService.createRoomArea(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoomAreaResponse>builder().result(response).build());
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<List<RoomAreaResponse>>> getRoomAreas(
            @RequestParam(name = "isActive", required = false) Boolean isActive
    ) {
        List<RoomAreaResponse> roomAreaResponseList = null;
        if (isActive == null) {
            roomAreaResponseList = roomAreaService.getRoomAreas();
        } else {
            roomAreaResponseList = roomAreaService.getRoomAreas(isActive);
        }
        return ResponseEntity.ok(ApiResponse.<List<RoomAreaResponse>>builder().result(roomAreaResponseList).build());
    }

    @GetMapping(path = "/info/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomAreaResponse>> getRoomAreaById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(ApiResponse.<RoomAreaResponse>builder()
                .result(roomAreaService.getRoomAreaById(id))
                .build());
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomAreaResponse>> updateRoomArea(
            @PathVariable("id") Long id,
            @Valid @RequestBody RoomAreaUpdateRequest request) {
        var response = roomAreaService.updateRoomArea(id, request);
        return ResponseEntity.ok(ApiResponse.<RoomAreaResponse>builder().result(response).build());
    }
}
