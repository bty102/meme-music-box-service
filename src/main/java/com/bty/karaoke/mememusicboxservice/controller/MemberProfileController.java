package com.bty.karaoke.mememusicboxservice.controller;

import com.bty.karaoke.mememusicboxservice.dto.request.MemberProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.ApiResponse;
import com.bty.karaoke.mememusicboxservice.dto.response.MemberProfileResponse;
import com.bty.karaoke.mememusicboxservice.service.MemberProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberProfiles")
@RequiredArgsConstructor
public class MemberProfileController {

    private final MemberProfileService memberProfileService;

    @PutMapping(path = "/{memberProfileId}", consumes = "application/json",produces = "application/json")
    public ResponseEntity<ApiResponse<MemberProfileResponse>> updateMemberProfile(
            @PathVariable("memberProfileId") Long memberProfileId,
            @Valid @RequestBody MemberProfileUpdateRequest request
    ) {
        var response = memberProfileService.updateMemberProfile(memberProfileId, request);
        return ResponseEntity.ok(
                ApiResponse.<MemberProfileResponse>builder()
                        .result(response)
                        .build()
        );
    }
}
