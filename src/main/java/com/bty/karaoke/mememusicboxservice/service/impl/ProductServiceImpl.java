package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductResponse;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.ProductMapper;
import com.bty.karaoke.mememusicboxservice.repository.ProductRepository;
import com.bty.karaoke.mememusicboxservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ProductResponse createProduct(@Valid ProductCreationRequest request) {
        if(productRepository.existsByProductCode(request.getProductCode())) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }

        if(productRepository.existsByProductName(request.getProductName())) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }

        Product product = productMapper.toProduct(request);
        product.setImageUrl(null);
        product.setIsActive(true);
        product.setCreatedAt(LocalDateTime.now());
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public Page<ProductResponse> getProducts(int pageNumber, int pageSize) {
        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;


        Sort sort = Sort.by("productName").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> getProducts(Boolean isActive, int pageNumber, int pageSize) {
        if(pageNumber < 0) pageNumber = 0;
        if(pageSize < 1) pageSize = 1;

        Sort sort = Sort.by("productName").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByIsActive(isActive, pageable);
        return productPage.map(productMapper::toProductResponse);
    }
}
