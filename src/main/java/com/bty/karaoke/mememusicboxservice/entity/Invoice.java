package com.bty.karaoke.mememusicboxservice.entity;

import com.bty.karaoke.mememusicboxservice.constant.InvoiceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Invoice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "InvoiceCode", unique = true, nullable = false, length = 20)
    private String invoiceCode;

    @Column(name = "RoomCharge", precision = 18, scale = 0)
    private BigDecimal roomCharge;

    @Column(name = "ServiceCharge", precision = 18, scale = 0)
    private BigDecimal serviceCharge;

    @Column(name = "DiscountPercent", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercent;

    @Column(name = "DiscountAmount", precision = 18, scale = 0)
    private BigDecimal discountAmount;

    @Column(name = "VatPercent", nullable = false, precision = 5, scale = 2)
    private BigDecimal vatPercent;

    @Column(name = "VatAmount", precision = 18, scale = 0)
    private BigDecimal vatAmount;

    @Column(name = "FinalAmount", precision = 18, scale = 0)
    private BigDecimal finalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, length = 20)
    private InvoiceStatus status;

    @Column(name = "CreatedAt", nullable = false, columnDefinition = "DATETIME2(3)")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "CreatedByAccountId", nullable = false)
    private Account createdByAccount;

    @ManyToOne
    @JoinColumn(name = "MemberAccountId")
    private Account memberAccount;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @Builder.Default
    private List<RoomOfInvoice> roomOfInvoices = new ArrayList<>();

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ProductOfInvoice> productOfInvoices = new ArrayList<>();
}
