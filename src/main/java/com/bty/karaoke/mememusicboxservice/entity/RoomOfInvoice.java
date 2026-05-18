package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "RoomOfInvoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomOfInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RoomId", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "InvoiceId", nullable = false)
    private Invoice invoice;

    @Column(name = "CheckInAt", nullable = false, columnDefinition = "DATETIME2(3)")
    private LocalDateTime checkInAt;

    @Column(name = "CheckOutAt", columnDefinition = "DATETIME2(3)")
    private LocalDateTime checkOutAt;

    @Column(name = "DurationHours", precision = 5, scale = 2)
    private BigDecimal durationHours;

    @Column(name = "RoomCharge", precision = 18, scale = 0)
    private BigDecimal roomCharge;

    @Column(name = "IsTransferred", nullable = false)
    private Boolean isTransferred;
}
