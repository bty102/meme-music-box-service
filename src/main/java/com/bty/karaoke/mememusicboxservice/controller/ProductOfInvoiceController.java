package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.service.ProductOfInvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
