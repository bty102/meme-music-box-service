package com.bty.karaoke.mememusicboxservice.dto.request;

import com.bty.karaoke.mememusicboxservice.constant.LogType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemAuditLogCreationRequest {

    @NotNull(message = "LOG_TYPE_NULL")
    private LogType logType;

    private Long invoiceId;

    @NotNull(message = "CHANGED_BY_ACCOUNT_ID_NULL")
    private Long changedByAccountId; // ID của người thực hiện

    // Lưu chuỗi JSON dữ liệu cũ.
    private Object oldValue;

    // Lưu chuỗi JSON dữ liệu mới.
    private Object newValue;


    private String description;
}
