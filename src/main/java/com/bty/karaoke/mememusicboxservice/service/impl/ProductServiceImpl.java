package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ImageUploadResult;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductResponse;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.ProductMapper;
import com.bty.karaoke.mememusicboxservice.repository.ProductRepository;
import com.bty.karaoke.mememusicboxservice.service.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final FileStorageService fileStorageService;

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

    @Override
    public Page<ProductResponse> findProductsByProductCodeContainingOrProductNameContaining(String productCode, String productName, int pageNumber, int pageSize) {

        Sort sort = Sort.by("productName").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByProductCodeContainingOrProductNameContaining(productCode, productName, pageable);
        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    public Page<ProductResponse> findProductsByProductCodeContainingOrProductNameContaining(String productCode, String productName, Boolean isActive, int pageNumber, int pageSize) {
        Sort sort = Sort.by("productName").ascending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        Page<Product> productPage = productRepository.findByProductCodeContainingAndIsActiveOrProductNameContainingAndIsActive(productCode, isActive, productName, isActive, pageable);
        return productPage.map(productMapper::toProductResponse);
    }

    @Override
    public ProductResponse getProductById(Long productId) {
        if(productId == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductResponse updateProduct(Long id, @Valid ProductUpdateRequest request) {

        if(id == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        if(productRepository.existsByProductCodeAndIdIsNot(request.getProductCode(), id)) {
            throw new AppException(ErrorCode.PRODUCT_CODE_EXISTED);
        }

        if(productRepository.existsByProductNameAndIdIsNot(request.getProductName(), id)) {
            throw new AppException(ErrorCode.PRODUCT_NAME_EXISTED);
        }

        productMapper.updateProduct(product, request);
        product = productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public void updateProductImage(Long productId, MultipartFile file) {
        if(productId == null) {
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));

        ImageUploadResult imageUploadResult = fileStorageService.uploadImage(file);
        product.setImageUrl(imageUploadResult.getImageUrl());
        productRepository.save(product);
    }
}
