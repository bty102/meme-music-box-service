package com.bty.karaoke.mememusicboxservice.entity;

import com.bty.karaoke.mememusicboxservice.constant.RoomBookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "RoomBooking")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoomBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "BookingTime", nullable = false, columnDefinition = "DATETIME2(0)")
    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private RoomBookingStatus status;

    @Column(name = "CreatedAt", nullable = false, columnDefinition = "DATETIME2(3)")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "RoomId", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "MemberAccountId", nullable = false)
    private Account memberAccount;
}
