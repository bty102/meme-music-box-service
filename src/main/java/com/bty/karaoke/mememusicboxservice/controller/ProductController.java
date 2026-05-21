package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductResponse;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import com.bty.karaoke.mememusicboxservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping(path = "", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductCreationRequest request) {
        var response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<ProductResponse>builder()
                        .result(response)
                        .build());
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProducts(
            @RequestParam(name = "isActive", required = false) Boolean isActive,
            @RequestParam(name = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", required = false, defaultValue = "1") int pageSize
    ) {
        Page<ProductResponse> response = null;
        if (isActive == null) {
            response = productService.getProducts(pageNumber, pageSize);
        } else {
            response = productService.getProducts(isActive, pageNumber, pageSize);
        }
        return ResponseEntity.ok(ApiResponse.<Page<ProductResponse>>builder()
                .result(response)
                .build());
    }
}
