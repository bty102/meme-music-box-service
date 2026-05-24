package com.bty.karaoke.mememusicboxservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomOfInvoiceCreationRequest {

    @NotNull(message = "ROOM_NOT_EXISTED")
    private Long roomId;

    @NotNull(message = "INVOICE_NOT_EXISTED")
    private Long invoiceId;

}
