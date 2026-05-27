package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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

    @GetMapping(path = "/paymentConfirmation", produces = "application/json")
    public ResponseEntity<ApiResponse<Void>> paymentConfirmation(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId
    ) {
        invoiceService.paymentConfirmation(invoiceId);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder().build()
        );
    }

    @GetMapping(path = "/temporary", produces = "application/json")
    public ResponseEntity<ApiResponse<InvoiceResponse>> temporaryInvoice(
            @RequestParam(name = "roomId", required = true) Long roomId
    ) {
        var response = invoiceService.getTemporaryInvoiceOfRoom(roomId);
        return ResponseEntity.ok(
                ApiResponse.<InvoiceResponse>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/ofRoom", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<InvoiceResponse>>> getInvoicesOfRoom(
            @RequestParam(name = "roomId", required = true) Long roomId,

            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        var response = invoiceService.getInvoicesOfRoom(roomId, pageNumber, pageSize);
        return ResponseEntity.ok(
                ApiResponse.<Page<InvoiceResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<InvoiceResponse>>> getInvoices(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        var response = invoiceService.getInvoices(pageNumber, pageSize);
        return ResponseEntity.ok(
                ApiResponse.<Page<InvoiceResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/createdBy", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<InvoiceResponse>>> getInvoicesCreatedBy(
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize,
            @AuthenticationPrincipal Jwt jwt
            ) {

        Long accountId = Long.parseLong(jwt.getClaims().get("accId").toString());
        var response = invoiceService.getInvoicesByCreatorAccId(accountId,  pageNumber, pageSize);
        return ResponseEntity.ok(
                ApiResponse.<Page<InvoiceResponse>>builder()
                        .result(response)
                        .build()
        );
    }

    @GetMapping(path = "/ofMember", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<InvoiceResponse>>> getInvoicesOfMember(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        Long accountId = Long.parseLong(jwt.getClaims().get("accId").toString());
        var response = invoiceService.getInvoicesOfMemberAccId(accountId, pageNumber, pageSize);
        return ResponseEntity.ok(
                ApiResponse.<Page<InvoiceResponse>>builder()
                        .result(response)
                        .build()
        );
    }
}
