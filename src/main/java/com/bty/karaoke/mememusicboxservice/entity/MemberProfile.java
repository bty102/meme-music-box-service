package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Entity
@Table(name = "MemberProfile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "MemberCode", unique = true, nullable = false, length = 20)
    private String memberCode;

    @Nationalized
    @Column(name = "FullName", nullable = false, length = 100)
    private String fullName;

    @Column(name = "IsMale", nullable = false)
    private Boolean isMale;

    @Column(name = "DateOfBirth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "LoyaltyPoint", nullable = false)
    private Integer loyaltyPoint;

    @Column(name = "ImageUrl", length = 500)
    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "AccountId", unique = true, nullable = false)
    private Account account;
}
