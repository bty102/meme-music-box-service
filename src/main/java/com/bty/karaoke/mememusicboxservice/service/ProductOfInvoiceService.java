package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.ProductOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ProductOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.ProductOfInvoice;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface ProductOfInvoiceService {

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#request.invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public ProductOfInvoiceResponse createProductOfInvoice(@Valid ProductOfInvoiceCreationRequest request);
}
