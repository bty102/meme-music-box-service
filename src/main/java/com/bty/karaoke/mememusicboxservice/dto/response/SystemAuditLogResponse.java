package com.bty.karaoke.mememusicboxservice.dto.response;

import com.bty.karaoke.mememusicboxservice.constant.LogType;

import java.time.LocalDateTime;

public class SystemAuditLogResponse {

    private Long id;

    private LogType logType;

    private Long invoiceId;

    private Long changedByAccountId; // ID của người thực hiện

    private LocalDateTime createdAt; // Thực hiện lúc nào

    // Lưu chuỗi JSON dữ liệu cũ.
    private String oldValue;

    // Lưu chuỗi JSON dữ liệu mới.
    private String newValue;

    private String description;
}
