package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;

@Entity
@Table(name = "PointDiscount")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "RequiredPoint", unique = true, nullable = false)
    private Integer requiredPoint;

    @Column(name = "DiscountPercent", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Nationalized
    @Column(name = "Description", length = 255)
    private String description;
}
