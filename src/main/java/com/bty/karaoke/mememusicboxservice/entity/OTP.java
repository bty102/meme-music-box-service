package com.bty.karaoke.mememusicboxservice.entity;

import com.bty.karaoke.mememusicboxservice.constant.OTPType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@Table(name = "OTP")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OTP {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "OtpCode", length = 255, unique = false ,nullable = false)
    private String otpCode;

    @Nationalized
    @Column(name = "Email", unique = false, nullable = false, length = 255)
    private String email;

    @Column(name = "ExpiryTime", unique = false, nullable = false)
    private LocalDateTime expiryTime;

    @Column(name = "CreatedAt", nullable = false, columnDefinition = "DATETIME2(3)")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", length = 20, unique = false, nullable = false)
    private OTPType type;

    @Column(name = "Verified", nullable = false, unique = false)
    private Boolean verified;
}
