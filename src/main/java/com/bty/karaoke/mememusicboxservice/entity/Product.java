package com.bty.karaoke.mememusicboxservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "ProductCode", unique = true, nullable = false, length = 20)
    private String productCode;

    @Nationalized
    @Column(name = "ProductName", unique = true, nullable = false, length = 100)
    private String productName;

    @Nationalized
    @Column(name = "Unit", nullable = false, length = 20)
    private String unit;

    @Column(name = "UnitPrice", nullable = false, precision = 18, scale = 0)
    private BigDecimal unitPrice;

    @Column(name = "StockQuantity", nullable = false)
    private Integer stockQuantity;

    @Column(name = "ImageUrl", length = 500)
    private String imageUrl;

    @Column(name = "IsActive", nullable = false)
    private Boolean isActive;

    @Column(name = "CreatedAt", nullable = false, columnDefinition = "DATETIME2(3)")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "product")
    @Builder.Default
    private List<ProductOfInvoice> productOfInvoices = new ArrayList<>();
}
