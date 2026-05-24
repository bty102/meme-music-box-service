package com.bty.karaoke.mememusicboxservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomOfInvoiceResponse {

    private Long id;

    private RoomResponse room;

    private InvoiceResponse invoice;

    private LocalDateTime checkInAt;

    private LocalDateTime checkOutAt;

    private BigDecimal durationHours;

    private BigDecimal roomCharge;

    private Boolean isTransferred;
}
