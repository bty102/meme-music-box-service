package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface InvoiceService {

    public InvoiceResponse createInvoice(@Valid InvoiceCreationRequest request);

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public InvoiceResponse updateMemberOfInvoice(Long invoiceId, Long memberAccountId);

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public InvoiceResponse deleteMemberOfInvoice(Long invoiceId);

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public void transferRoomOfInvoice(Long invoiceId, Long transferToRoomId);
}
