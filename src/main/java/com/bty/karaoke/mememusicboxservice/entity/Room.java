package com.bty.karaoke.mememusicboxservice.entity;

import com.bty.karaoke.mememusicboxservice.constant.RoomStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Room")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "RoomNumber", unique = true, nullable = false)
    private Integer roomNumber;

    @Column(name = "Capacity", nullable = false)
    private Integer capacity;

    @Column(name = "HourlyRate", nullable = false, precision = 18, scale = 0)
    private BigDecimal hourlyRate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private RoomStatus status;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "AreaId", nullable = false)
    private RoomArea area;

    @OneToMany(mappedBy = "room")
    @Builder.Default
    private List<RoomOfInvoice> roomOfInvoices = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    @Builder.Default
    private List<RoomBooking> roomBookings = new ArrayList<>();
}
