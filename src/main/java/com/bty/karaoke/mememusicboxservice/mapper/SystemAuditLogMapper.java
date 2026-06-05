package com.bty.karaoke.mememusicboxservice.mapper;

import com.bty.karaoke.mememusicboxservice.dto.request.SystemAuditLogCreationRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.SystemAuditLogResponse;
import com.bty.karaoke.mememusicboxservice.entity.SystemAuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SystemAuditLogMapper {

    public SystemAuditLogResponse toSystemAuditLogResponse(SystemAuditLog systemAuditLog);

    @Mapping(target = "newValue", ignore = true)
    @Mapping(target = "oldValue", ignore = true)
    public SystemAuditLog toSystemAuditLog(SystemAuditLogCreationRequest request);
}
