package com.bty.karaoke.mememusicboxservice.service.impl;

import com.bty.karaoke.mememusicboxservice.dto.request.MemberProfileUpdateRequest;
import com.bty.karaoke.mememusicboxservice.dto.response.MemberProfileResponse;
import com.bty.karaoke.mememusicboxservice.entity.MemberProfile;
import com.bty.karaoke.mememusicboxservice.exception.AppException;
import com.bty.karaoke.mememusicboxservice.exception.ErrorCode;
import com.bty.karaoke.mememusicboxservice.mapper.MemberProfileMapper;
import com.bty.karaoke.mememusicboxservice.repository.MemberProfileRepository;
import com.bty.karaoke.mememusicboxservice.service.MemberProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class MemberProfileServiceImpl implements MemberProfileService {

    private final MemberProfileRepository memberProfileRepository;
    private final MemberProfileMapper memberProfileMapper;

    @Override
    public MemberProfileResponse updateMemberProfile(Long id, @Valid MemberProfileUpdateRequest request) {
        if(id==null){
            throw new AppException(ErrorCode.MEMBER_PROFILE_NOT_EXISTED);
        }
        MemberProfile memberProfile = memberProfileRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.MEMBER_PROFILE_NOT_EXISTED));

        memberProfileMapper.updateMemberProfile(memberProfile, request);
        memberProfile = memberProfileRepository.save(memberProfile);
        return memberProfileMapper.toMemberProfileResponse(memberProfile);
    }
}
