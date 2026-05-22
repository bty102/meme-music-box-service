package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductResponse;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     *
     * @param productCode
     * @param productName
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<ProductResponse> findProductsByProductCodeContainingOrProductNameContaining(String productCode, String productName, int pageNumber, int pageSize);

    /**
     *
     * @param productCode
     * @param productName
     * @param isActive
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<ProductResponse> findProductsByProductCodeContainingOrProductNameContaining(String productCode, String productName, Boolean isActive, int pageNumber, int pageSize);

    public ProductResponse getProductById(Long productId);

    public ProductResponse updateProduct(Long id, @Valid ProductUpdateRequest request);

    public void updateProductImage(Long productId, MultipartFile file);
}
