package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomOfInvoiceResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface RoomOfInvoiceService {

    public RoomOfInvoiceResponse createRoomOfInvoice(@Valid RoomOfInvoiceCreationRequest request);

    @PreAuthorize("""
        @invoiceRepository.existsByIdAndMemberAccount_Email(#invoiceId, authentication.principal.getSubject())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
        or
        hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).EMPLOYEE.name())
    """)
    public List<RoomOfInvoiceResponse> getRoomsOfInvoice(Long invoiceId);
}
