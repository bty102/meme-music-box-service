package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.service.RoomOfInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roomOfInvoice")
@RequiredArgsConstructor
public class RoomOfInvoiceController {

    private final RoomOfInvoiceService roomOfInvoiceService;

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<List<RoomOfInvoiceResponse>>> getRoomsOfInvoiceByInvoiceId(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId
    ) {
        var response = roomOfInvoiceService.getRoomsOfInvoice(invoiceId);
        return ResponseEntity.ok(
                ApiResponse.<List<RoomOfInvoiceResponse>>builder()
                        .result(response)
                        .build()
        );
    }
}
