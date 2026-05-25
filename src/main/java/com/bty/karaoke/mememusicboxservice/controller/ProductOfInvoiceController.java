package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.service.ProductOfInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/productOfInvoice")
@RequiredArgsConstructor
public class ProductOfInvoiceController {

    private final ProductOfInvoiceService productOfInvoiceService;

    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<ProductOfInvoiceResponse>> createProductOfInvoice(
            @Valid @RequestBody ProductOfInvoiceCreationRequest request
    ) {
        var response = productOfInvoiceService.createProductOfInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProductOfInvoiceResponse>builder()
                        .result(response)
                        .build());
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<ProductOfInvoiceResponse>> updateProductOfInvoice(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductOfInvoiceUpdateRequest request
    ) {
        var response = productOfInvoiceService.updateProductOfInvoice(id, request);
        return ResponseEntity.ok(ApiResponse.<ProductOfInvoiceResponse>builder()
                .result(response)
                .build());
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<List<ProductOfInvoiceResponse>>> getProductsOfInvoiceByInvoiceId(
            @RequestParam(name = "invoiceId", required = true) Long invoiceId
    ) {
        var response = productOfInvoiceService.getProductsOfInvoice(invoiceId);
        return ResponseEntity.ok(
                ApiResponse.<List<ProductOfInvoiceResponse>>builder()
                        .result(response)
                        .build()
        );
    }
}
