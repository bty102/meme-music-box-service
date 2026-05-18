package com.bty.karaoke.mememusicboxservice.entity;


import com.bty.karaoke.mememusicboxservice.constant.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Nationalized
    @Column(name = "Email", unique = true, nullable = false, length = 255)
    private String email;

    @Column(name = "PasswordHash", nullable = false, length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false, length = 20)
    private Role role;

    @Column(name = "CreatedAt", nullable = false, columnDefinition = "DATETIME2(3)")
    private LocalDateTime createdAt;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private EmployeeProfile employeeProfile;

    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    private MemberProfile memberProfile;

    @OneToMany(mappedBy = "createdByAccount")
    @Builder.Default
    private List<Invoice> createdInvoices = new ArrayList<>();

    @OneToMany(mappedBy = "memberAccount")
    @Builder.Default
    private List<Invoice> memberInvoices = new ArrayList<>();

    @OneToMany(mappedBy = "memberAccount")
    @Builder.Default
    private List<RoomBooking> roomBookings = new ArrayList<>();
}
