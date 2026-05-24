package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.InvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.InvoiceResponse;
import com.bty.karaoke.mememusicboxservice.entity.Invoice;
import jakarta.validation.Valid;

public interface InvoiceService {

    public InvoiceResponse createInvoice(@Valid InvoiceCreationRequest request);
}
