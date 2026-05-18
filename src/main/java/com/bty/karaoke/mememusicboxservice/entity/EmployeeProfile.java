package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Table(name = "EmployeeProfile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "EmployeeCode", unique = true, nullable = false, length = 20)
    private String employeeCode;

    @Nationalized
    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "PhoneNumber", unique = true, nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "NationalId", unique = true, nullable = false, length = 12)
    private String nationalId;

    @Column(name = "IsMale", nullable = false)
    private Boolean isMale;

    @Column(name = "DateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @Nationalized
    @Column(name = "Address", length = 255)
    private String address;

    @Column(name = "ImageUrl", length = 500)
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "AccountId", unique = true, nullable = false)
    private Account account;
}
