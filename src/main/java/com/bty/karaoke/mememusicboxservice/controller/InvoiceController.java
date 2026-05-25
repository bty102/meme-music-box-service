package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping(path = "/updateMember", produces = "application/json")
    public ResponseEntity<ApiResponse<InvoiceResponse>> updateMemberOfInvoice(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId,
            @RequestParam(name = "memberAccId", required = true) Long memberAccountId
    ) {
        var response = invoiceService.updateMemberOfInvoice(invoiceId, memberAccountId);
        return ResponseEntity.ok(ApiResponse.<InvoiceResponse>builder()
                .result(response)
                .build());
    }

    @GetMapping(path = "/deleteMember", produces = "application/json")
    public ResponseEntity<ApiResponse<InvoiceResponse>> deleteMemberOfInvoice(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId
    ) {
        var response = invoiceService.deleteMemberOfInvoice(invoiceId);
        return ResponseEntity.ok(ApiResponse.<InvoiceResponse>builder()
                .result(response)
                .build());
    }

    @GetMapping(path = "/transferToRoom", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> transferToRoom(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId,
            @RequestParam(name = "roomId", required = true) Long roomId
    ) {
        invoiceService.transferRoomOfInvoice(invoiceId, roomId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder().build()
        );
    }

    @GetMapping(path = "/checkOut", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> checkOut(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId
    ) {
        invoiceService.checkOut(invoiceId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder().build()
        );
    }
}
