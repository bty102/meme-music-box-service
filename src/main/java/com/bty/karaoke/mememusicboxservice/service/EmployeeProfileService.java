package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.EmployeeProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.EmployeeProfileResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface EmployeeProfileService {

    @PreAuthorize("hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())")
    public EmployeeProfileResponse updateEmployeeProfile(Long profileId, @Valid EmployeeProfileUpdateRequest request);
}
