package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.SystemAuditLogCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.SystemAuditLogResponse;
import com.bty.karaoke.mememusicboxservice.entity.SystemAuditLog;
import com.bty.karaoke.mememusicboxservice.mapper.SystemAuditLogMapper;
import com.bty.karaoke.mememusicboxservice.repository.SystemAuditLogRepository;
import com.bty.karaoke.mememusicboxservice.service.SystemAuditLogService;
import com.bty.karaoke.mememusicboxservice.util.JsonUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Validated
public class SystemAuditLogServiceImpl implements SystemAuditLogService {

    private final SystemAuditLogRepository systemAuditLogRepository;
    private final SystemAuditLogMapper systemAuditLogMapper;
    private final JsonUtil jsonUtil;

    @Override
    public SystemAuditLogResponse createSystemAuditLog(@Valid SystemAuditLogCreationRequest request) {
        SystemAuditLog systemAuditLog =  systemAuditLogMapper.toSystemAuditLog(request);
        systemAuditLog.setOldValue(jsonUtil.toJson(request.getOldValue()));
        systemAuditLog.setNewValue(jsonUtil.toJson(request.getNewValue()));
        systemAuditLog.setCreatedAt(LocalDateTime.now());

        systemAuditLog =  systemAuditLogRepository.save(systemAuditLog);
        return systemAuditLogMapper.toSystemAuditLogResponse(systemAuditLog);
    }
}
