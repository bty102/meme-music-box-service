package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomResponse;
import com.bty.karaoke.mememusicboxservice.service.RoomService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomResponse>> createRoom(
            @Valid @RequestBody RoomCreationRequest request
    ) {
        var response = roomService.createRoom(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RoomResponse>builder()
                        .result(response)
                        .build());
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<RoomResponse>>> findRoomsByAreaId(
            @RequestParam(name = "areaId", required = true) Long areaId,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        var response = roomService.findRoomsByAreaId(areaId, pageNumber, pageSize);
        return ResponseEntity.ok(ApiResponse.<Page<RoomResponse>>builder()
                .result(response)
                .build());
    }

    @GetMapping(path = "info/{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<RoomResponse>> findRoomById(@PathVariable("id") Long id) {
        var response = roomService.findRoomById(id);
        return ResponseEntity.ok(ApiResponse.<RoomResponse>builder()
                .result(response)
                .build());
    }
}
