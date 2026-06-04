package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.EmployeeProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.EmployeeProfileResponse;
import com.bty.karaoke.mememusicboxservice.service.EmployeeProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employeeProfiles")
@RequiredArgsConstructor
public class EmployeeProfileController {

    private final EmployeeProfileService employeeProfileService;

    @PutMapping(path = "{profileId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ApiResponse<EmployeeProfileResponse>> updateEmployeeProfile(
            @PathVariable("profileId") Long profileId,
            @Valid @RequestBody EmployeeProfileUpdateRequest request
            ) {
        var response = employeeProfileService.updateEmployeeProfile(profileId, request);
        return ResponseEntity.ok(
                ApiResponse.<EmployeeProfileResponse>builder()
                        .result(response)
                        .build()
        );
    }
}
