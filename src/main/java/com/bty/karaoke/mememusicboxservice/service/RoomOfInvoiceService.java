package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.RoomOfInvoiceCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.RoomOfInvoiceResponse;
import jakarta.validation.Valid;

public interface RoomOfInvoiceService {

    public RoomOfInvoiceResponse createRoomOfInvoice(@Valid RoomOfInvoiceCreationRequest request);
}
