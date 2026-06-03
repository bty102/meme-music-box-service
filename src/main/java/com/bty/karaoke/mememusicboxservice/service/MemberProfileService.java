package com.bty.karaoke.mememusicboxservice.service;

import com.bty.karaoke.mememusicboxservice.dto.request.MemberProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.MemberProfileResponse;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;

public interface MemberProfileService {

    @PreAuthorize("""
    @memberProfileRepository.existsByIdAndAccount_Email(#id, authentication.principal.getSubject())
    or
    hasRole(T(com.bty.karaoke.mememusicboxservice.constant.Role).ADMIN.name())
""")
    public MemberProfileResponse updateMemberProfile(Long id, @Valid MemberProfileUpdateRequest request);
}
