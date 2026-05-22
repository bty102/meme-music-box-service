package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.PointDiscountUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.PointDiscountResponse;
import com.bty.karaoke.mememusicboxservice.entity.PointDiscount;
import com.bty.karaoke.mememusicboxservice.service.PointDiscountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/pointDiscounts")
public class PointDiscountController {

    private final PointDiscountService pointDiscountService;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<PointDiscountResponse>> createPointDiscount(
            @Valid @RequestBody PointDiscountCreationRequest request
    ) {
        var response = pointDiscountService.createPointDiscount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<PointDiscountResponse>builder()
                        .result(response)
                        .build());
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<List<PointDiscountResponse>>> getPointDiscounts() {
        var response = pointDiscountService.findAllPointDiscounts();
        return ResponseEntity.ok(ApiResponse.<List<PointDiscountResponse>>builder()
                .result(response)
                .build());
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<PointDiscountResponse>> updatePointDiscount(
            @PathVariable("id") Long id,
            @Valid @RequestBody PointDiscountUpdateRequest request
    ) {
        var response = pointDiscountService.updatePointDiscount(id, request);
        return ResponseEntity.ok(ApiResponse.<PointDiscountResponse>builder()
                .result(response)
                .build());
    }

    @DeleteMapping(path = "{id}", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> deletePointDiscount(
        @PathVariable("id") Long id
    ) {
        pointDiscountService.deletePointDiscount(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}
