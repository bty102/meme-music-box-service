package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.ProductOfInvoice;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface ProductOfInvoiceService {

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#request.invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public ProductOfInvoiceResponse createProductOfInvoice(@Valid ProductOfInvoiceCreationRequest request);

    @PreAuthorize("""
                @productOfInvoiceRepository.existsByIdAndInvoice_CreatedByAccount_Email(#id,  authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public ProductOfInvoiceResponse updateProductOfInvoice(Long id, @Valid ProductOfInvoiceUpdateRequest request);

    @PreAuthorize("""
        @invoiceRepository.existsByIdAndMemberAccount_Email(#invoiceId, authentication.principal.getSubject())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public List<ProductOfInvoiceResponse> getProductsOfInvoice(Long invoiceId);
}
