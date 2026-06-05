package com.bty.karaoke.mememusicboxservice.entity;

import com.bty.karaoke.mememusicboxservice.constant.LogType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Entity
@Table(name = "SystemAuditLog")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SystemAuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "LogType", nullable = false, length = 50)
    private LogType logType;

    @Column(name = "InvoiceId")
    private Long invoiceId;

    @Column(name = "ChangedByAccountId", nullable = false)
    private Long changedByAccountId; // ID của người thực hiện

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createdAt; // Thực hiện lúc nào

    @Lob
    @Nationalized
    @Column(name = "OldValue")
    // Lưu chuỗi JSON dữ liệu cũ.
    private String oldValue;

    @Lob
    @Nationalized
    @Column(name = "NewValue")
    // Lưu chuỗi JSON dữ liệu mới.
    private String newValue;

    @Nationalized
    @Column(name = "Description", length = 500)
    private String description;
}
