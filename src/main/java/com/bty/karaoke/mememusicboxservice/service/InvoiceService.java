package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

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

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public void checkOut(Long invoiceId);

    @PreAuthorize("""
                @invoiceRepository.existsByIdAndCreatedByAccount_Email(#invoiceId, authentication.principal.getSubject())
                or
                hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
            """)
    public void paymentConfirmation(Long invoiceId);

    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public InvoiceResponse getTemporaryInvoiceOfRoom(Long roomId);

    /**
     *
     * @param roomId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public Page<InvoiceResponse> getInvoicesOfRoom(Long roomId, int pageNumber, int pageSize);

    /**
     *
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    @PreAuthorize("""
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public Page<InvoiceResponse> getInvoices(int pageNumber, int pageSize);

    /**
     *
     * @param creatorAccountId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<InvoiceResponse> getInvoicesByCreatorAccId(Long creatorAccountId, int pageNumber, int pageSize);

    /**
     *
     * @param memberAccountId
     * @param pageNumber must be >= 0
     * @param pageSize must be >= 1
     * @return
     */
    public Page<InvoiceResponse> getInvoicesOfMemberAccId(Long memberAccountId, int pageNumber, int pageSize);

    @PreAuthorize("""       
        @invoiceRepository.existsByIdAndMemberAccount_Email(#invoiceId, authentication.principal.getSubject())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public InvoiceResponse getInvoice(Long invoiceId);

    String buildInvoiceHtml(Long invoiceId);

    byte[] exportInvoicePDF(Long invoiceId);
}
