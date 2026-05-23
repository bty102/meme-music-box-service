package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductResponse;
import com.bty.karaoke.mememusicboxservice.entity.Product;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public ProductResponse createProduct(@Valid ProductCreationRequest request);

    /**
     *
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public Page<ProductResponse> getProducts(int pageNumber, int pageSize);

    @PreAuthorize("#isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public Page<ProductResponse> getProducts(Boolean isActive, int pageNumber, int pageSize);

    /**
     *
     * @param productCode
     * @param productName
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
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
    @PreAuthorize("#isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public Page<ProductResponse> findProductsByProductCodeContainingOrProductNameContaining(String productCode, String productName, Boolean isActive, int pageNumber, int pageSize);

    @PostAuthorize("returnObject.isActive == true or hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public ProductResponse getProductById(Long productId);

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public ProductResponse updateProduct(Long id, @Valid ProductUpdateRequest request);

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public void updateProductImage(Long productId, MultipartFile file);
}
