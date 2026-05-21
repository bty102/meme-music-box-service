package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductResponse;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface ProductService {

    public ProductResponse createProduct(@Valid ProductCreationRequest request);

    /**
     *
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<ProductResponse> getProducts(int pageNumber, int pageSize);

    public Page<ProductResponse> getProducts(Boolean isActive, int pageNumber, int pageSize);
}
