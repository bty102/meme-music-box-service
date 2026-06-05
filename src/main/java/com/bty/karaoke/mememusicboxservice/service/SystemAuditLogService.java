package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.SystemAuditLogCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.SystemAuditLogResponse;
import jakarta.validation.Valid;

public interface SystemAuditLogService {

    public SystemAuditLogResponse createSystemAuditLog(@Valid SystemAuditLogCreationRequest request);
}
